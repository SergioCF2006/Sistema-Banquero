package com.banquero.algorithm;

import com.banquero.model.BankerModel;
import com.banquero.model.RequestResult;
import com.banquero.model.SafetyResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementación matemática rigurosa del Algoritmo del Banquero de Edsger Dijkstra.
 * Contiene:
 * 1. Algoritmo de Seguridad (Safety Algorithm).
 * 2. Algoritmo de Solicitud de Recursos (Resource-Request Algorithm).
 * 3. Registro pedagógico paso a paso para explicaciones en presentaciones universitarias.
 */
public class BankerAlgorithm {

    /**
     * Calcula la matriz Need: Need[i][j] = Max[i][j] - Allocation[i][j].
     * 
     * @param model Modelo con las matrices del sistema
     */
    public static void calculateNeed(BankerModel model) {
        model.calculateNeed();
    }

    /**
     * Ejecuta el Algoritmo de Seguridad para determinar si el estado actual es Seguro o Inseguro.
     *
     * @param model El modelo del sistema con sus matrices y vector disponible
     * @return SafetyResult con el veredicto, la secuencia segura y la bitácora paso a paso
     */
    public static SafetyResult checkSafety(BankerModel model) {
        int numProcesses = model.getNumProcesses();
        int numResources = model.getNumResources();

        // 1. Asegurar que la matriz Need esté actualizada
        model.calculateNeed();

        int[][] allocation = model.getAllocation();
        int[][] need = model.getNeed();
        int[] available = model.getAvailable();

        // Paso 1 del algoritmo:
        // Work = Available
        // Finish[i] = false para todo i
        int[] work = Arrays.copyOf(available, numResources);
        boolean[] finish = new boolean[numProcesses];

        List<Integer> safeSequence = new ArrayList<>();
        List<String> logSteps = new ArrayList<>();

        logSteps.add("══════════════════════════════════════════════════════════════");
        logSteps.add("              INICIO DEL ALGORITMO DE SEGURIDAD               ");
        logSteps.add("══════════════════════════════════════════════════════════════");
        logSteps.add("Vector de Recursos Disponibles inicial (Work = Available):");
        logSteps.add("  Work = " + vectorToString(work, numResources));
        logSteps.add("Todos los procesos marcados inicialmente como Finish = false.");
        logSteps.add("──────────────────────────────────────────────────────────────");

        int iteration = 1;
        boolean processFoundInIteration;

        // Paso 2 y 3 del algoritmo:
        // Buscar un proceso i tal que Finish[i] == false y Need[i] <= Work
        do {
            processFoundInIteration = false;
            logSteps.add("[Ronda " + iteration + " de evaluación]");

            for (int i = 0; i < numProcesses; i++) {
                if (!finish[i]) {
                    // Verificar si Need[i] <= Work para todos los recursos
                    boolean canExecute = true;
                    StringBuilder compareSb = new StringBuilder();
                    compareSb.append("  Evaluando Proceso P").append(i).append(": ");
                    compareSb.append("Need=").append(vectorToString(need[i], numResources))
                             .append(" vs Work=").append(vectorToString(work, numResources));

                    for (int j = 0; j < numResources; j++) {
                        if (need[i][j] > work[j]) {
                            canExecute = false;
                            break;
                        }
                    }

                    if (canExecute) {
                        compareSb.append("  ->  [SÍ PUEDE EJECUTARSE]");
                        logSteps.add(compareSb.toString());

                        // Paso 3:
                        // Work = Work + Allocation[i]
                        // Finish[i] = true
                        int[] workBefore = Arrays.copyOf(work, numResources);
                        for (int j = 0; j < numResources; j++) {
                            work[j] += allocation[i][j];
                        }
                        finish[i] = true;
                        safeSequence.add(i);
                        processFoundInIteration = true;

                        logSteps.add("    -> P" + i + " completa su trabajo y libera sus recursos.");
                        logSteps.add("    -> Allocation liberado: " + vectorToString(allocation[i], numResources));
                        logSteps.add("    -> Nuevo Work = " + vectorToString(workBefore, numResources)
                                + " + " + vectorToString(allocation[i], numResources)
                                + " = " + vectorToString(work, numResources));
                        logSteps.add("    -> Finish[P" + i + "] = TRUE");
                        logSteps.add("    -> Secuencia actual: " + buildPartialSequence(safeSequence));
                        logSteps.add("");
                    } else {
                        compareSb.append("  ->  [NO PUEDE EJECUTARSE (Insuficiente Work)]");
                        logSteps.add(compareSb.toString());
                    }
                }
            }

            iteration++;
        } while (processFoundInIteration && safeSequence.size() < numProcesses);

        // Paso 4:
        // Si Finish[i] == true para todo i, el sistema está en estado seguro.
        boolean isSafe = true;
        List<Integer> unfinished = new ArrayList<>();

        for (int i = 0; i < numProcesses; i++) {
            if (!finish[i]) {
                isSafe = false;
                unfinished.add(i);
            }
        }

        logSteps.add("──────────────────────────────────────────────────────────────");
        logSteps.add("                    DICTAMEN FINAL                            ");
        logSteps.add("──────────────────────────────────────────────────────────────");

        if (isSafe) {
            logSteps.add("ESTADO: SISTEMA SEGURO (SAFE STATE)");
            logSteps.add("Todos los procesos pudieron finalizar satisfactoriamente.");
            logSteps.add("Secuencia Segura encontrada: " + buildPartialSequence(safeSequence));
        } else {
            logSteps.add("ESTADO: SISTEMA INSEGURO (UNSAFE STATE)");
            logSteps.add("PELIGRO: Posible interbloqueo (Deadlock).");
            logSteps.add("Procesos que no pudieron ejecutarse por falta de recursos: " + unfinishedToString(unfinished));
            logSteps.add("Recursos finales Work disponibles que resultaron insuficientes: " + vectorToString(work, numResources));
        }
        logSteps.add("══════════════════════════════════════════════════════════════");

        return new SafetyResult(isSafe, safeSequence, logSteps, unfinished);
    }

    /**
     * Ejecuta el Algoritmo de Solicitud de Recursos (Resource-Request Algorithm).
     *
     * @param model El modelo con el estado original del sistema
     * @param processIndex Índice del proceso solicitante (ej. 1 para P1)
     * @param requestVector Vector con las cantidades solicitadas para cada recurso
     * @return RequestResult con la aprobación/rechazo y análisis detallado
     */
    public static RequestResult evaluateRequest(BankerModel model, int processIndex, int[] requestVector) {
        int numResources = model.getNumResources();
        StringBuilder log = new StringBuilder();

        log.append("══════════════════════════════════════════════════════════════\n");
        log.append("           EVALUACIÓN DE SOLICITUD DE RECURSOS               \n");
        log.append("══════════════════════════════════════════════════════════════\n");
        log.append("Proceso solicitante: P").append(processIndex).append("\n");
        log.append("Vector de Solicitud (Request): ").append(vectorToString(requestVector, numResources)).append("\n\n");

        // Condición 1: Request <= Need
        int[] needP = model.getNeed()[processIndex];
        log.append("Paso 1: ¿Request <= Need[P").append(processIndex).append("]?\n");
        log.append("  Request = ").append(vectorToString(requestVector, numResources)).append("\n");
        log.append("  Need    = ").append(vectorToString(needP, numResources)).append("\n");

        for (int j = 0; j < numResources; j++) {
            if (requestVector[j] > needP[j]) {
                log.append("  -> FALLO: Para el recurso R").append(j)
                   .append(", la solicitud (").append(requestVector[j])
                   .append(") supera la necesidad declarada (").append(needP[j]).append(").\n\n");
                log.append("DICTAMEN: Solicitud INVÁLIDA. El proceso ha superado su reclamo máximo.\n");
                return new RequestResult(
                        false,
                        "Solicitud inválida: El proceso P" + processIndex + " solicita más recursos de los que declaró necesitar (Request > Need en R" + j + ").",
                        log.toString(),
                        null,
                        null
                );
            }
        }
        log.append("  -> OK: Request <= Need cumplido para todos los recursos.\n\n");

        // Condición 2: Request <= Available
        int[] available = model.getAvailable();
        log.append("Paso 2: ¿Request <= Available?\n");
        log.append("  Request   = ").append(vectorToString(requestVector, numResources)).append("\n");
        log.append("  Available = ").append(vectorToString(available, numResources)).append("\n");

        for (int j = 0; j < numResources; j++) {
            if (requestVector[j] > available[j]) {
                log.append("  -> FALLO: Para el recurso R").append(j)
                   .append(", la solicitud (").append(requestVector[j])
                   .append(") supera los recursos disponibles (").append(available[j]).append(").\n\n");
                log.append("DICTAMEN: Solicitud NO CONCEDIDA. Recursos insuficientes actualmente. El proceso P")
                   .append(processIndex).append(" debe esperar.\n");
                return new RequestResult(
                        false,
                        "Solicitud no concedida: No existen suficientes recursos disponibles actualmente (Request > Available en R" + j + "). El proceso P" + processIndex + " debe esperar.",
                        log.toString(),
                        null,
                        null
                );
            }
        }
        log.append("  -> OK: Request <= Available cumplido para todos los recursos.\n\n");

        // Paso 3: Asignación tentativa / simulación
        log.append("Paso 3: Simulación temporal de asignación preventiva...\n");
        log.append("  Se asignan provisionalmente los recursos al proceso P").append(processIndex).append(":\n");

        BankerModel simulatedModel = model.cloneState();
        int[] simAvail = simulatedModel.getAvailable();
        int[][] simAlloc = simulatedModel.getAllocation();
        int[][] simNeed = simulatedModel.getNeed();

        for (int j = 0; j < numResources; j++) {
            simAvail[j] -= requestVector[j];
            simAlloc[processIndex][j] += requestVector[j];
            simNeed[processIndex][j] -= requestVector[j];
        }

        log.append("  Available temporal = ").append(vectorToString(simAvail, numResources)).append("\n");
        log.append("  Allocation P").append(processIndex).append(" temporal = ").append(vectorToString(simAlloc[processIndex], numResources)).append("\n");
        log.append("  Need P").append(processIndex).append(" temporal = ").append(vectorToString(simNeed[processIndex], numResources)).append("\n\n");

        // Paso 4: Evaluar algoritmo de seguridad sobre el estado simulado
        log.append("Paso 4: Ejecutando Algoritmo de Seguridad sobre el estado simulado...\n");
        SafetyResult safetyResult = checkSafety(simulatedModel);

        if (safetyResult.isSafe()) {
            log.append("\n==============================================================\n");
            log.append("DICTAMEN: SOLICITUD APROBADA\n");
            log.append("El sistema PERMANECE EN ESTADO SEGURO tras la asignación.\n");
            log.append("Secuencia segura garantizada: ").append(safetyResult.getFormattedSequence()).append("\n");
            log.append("Los recursos pueden asignarse a P").append(processIndex).append(" de inmediato.\n");
            log.append("==============================================================\n");

            return new RequestResult(
                    true,
                    "Solicitud APROBADA: El sistema permanece en estado seguro. Secuencia segura: " + safetyResult.getFormattedSequence(),
                    log.toString(),
                    safetyResult,
                    simulatedModel
            );
        } else {
            log.append("\n==============================================================\n");
            log.append("DICTAMEN: SOLICITUD DENEGADA\n");
            log.append("La asignación provocaría un ESTADO INSEGURO (posible interbloqueo).\n");
            log.append("Por política de prevención del banquero, el proceso P").append(processIndex).append(" debe esperar.\n");
            log.append("El estado original del sistema se mantiene INTACTO sin modificaciones.\n");
            log.append("==============================================================\n");

            return new RequestResult(
                    false,
                    "Solicitud DENEGADA: La asignación provocaría un estado inseguro (riesgo de interbloqueo). El estado original no fue modificado.",
                    log.toString(),
                    safetyResult,
                    null
            );
        }
    }

    // ================= FUNCIONES AUXILIARES DE FORMATO =================

    public static String vectorToString(int[] vector, int size) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(vector[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String buildPartialSequence(List<Integer> seq) {
        if (seq.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seq.size(); i++) {
            sb.append("P").append(seq.get(i));
            if (i < seq.size() - 1) {
                sb.append(" → ");
            }
        }
        return sb.toString();
    }

    private static String unfinishedToString(List<Integer> list) {
        if (list.isEmpty()) {
            return "Ninguno";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append("P").append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
