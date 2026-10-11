package com.banquero.model;

import java.util.Arrays;

/**
 * Modelo que encapsula los datos del Algoritmo del Banquero:
 * - Cantidad de Procesos y Tipos de Recursos.
 * - Matriz de Asignación (Allocation).
 * - Matriz de Demanda Máxima (Max).
 * - Matriz de Necesidad Futura (Need).
 * - Vector de Recursos Disponibles (Available).
 */
public class BankerModel {

    private int numProcesses;
    private int numResources;

    private int[][] allocation;
    private int[][] max;
    private int[][] need;
    private int[] available;

    /**
     * Constructor principal con dimensiones dinámicas.
     *
     * @param numProcesses Cantidad de procesos (filas)
     * @param numResources Cantidad de recursos (columnas)
     */
    public BankerModel(int numProcesses, int numResources) {
        if (numProcesses <= 0 || numResources <= 0) {
            throw new IllegalArgumentException("El número de procesos y recursos debe ser mayor a 0.");
        }
        this.numProcesses = numProcesses;
        this.numResources = numResources;

        this.allocation = new int[numProcesses][numResources];
        this.max = new int[numProcesses][numResources];
        this.need = new int[numProcesses][numResources];
        this.available = new int[numResources];
    }

    /**
     * Calcula la matriz Need según la fórmula:
     * Need[i][j] = Max[i][j] - Allocation[i][j]
     */
    public void calculateNeed() {
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                need[i][j] = max[i][j] - allocation[i][j];
            }
        }
    }

    /**
     * Valida la coherencia de los datos del sistema.
     *
     * @return Mensaje con el error encontrado, o null si todo es correcto.
     */
    public String validateData() {
        // Validar Vector Available
        for (int j = 0; j < numResources; j++) {
            if (available[j] < 0) {
                return "El recurso disponible R" + j + " no puede ser negativo (" + available[j] + ").";
            }
        }

        // Validar Matrices Allocation y Max
        for (int i = 0; i < numProcesses; i++) {
            for (int j = 0; j < numResources; j++) {
                int allocVal = allocation[i][j];
                int maxVal = max[i][j];

                if (allocVal < 0) {
                    return "Allocation[P" + i + "][R" + j + "] no puede ser negativo (" + allocVal + ").";
                }
                if (maxVal < 0) {
                    return "Max[P" + i + "][R" + j + "] no puede ser negativo (" + maxVal + ").";
                }
                if (maxVal < allocVal) {
                    return "Inconsistencia en P" + i + ", R" + j + ": Max (" + maxVal 
                            + ") es menor que Allocation (" + allocVal + "). "
                            + "Un proceso no puede tener asignado más de lo que declaró como máximo.";
                }
            }
        }
        return null;
    }

    /**
     * Crea un clon profundo del estado actual del modelo.
     * Esto permite simular solicitudes de recursos sin alterar el estado real.
     */
    public BankerModel cloneState() {
        BankerModel copy = new BankerModel(this.numProcesses, this.numResources);
        copy.available = Arrays.copyOf(this.available, this.numResources);

        for (int i = 0; i < this.numProcesses; i++) {
            copy.allocation[i] = Arrays.copyOf(this.allocation[i], this.numResources);
            copy.max[i] = Arrays.copyOf(this.max[i], this.numResources);
            copy.need[i] = Arrays.copyOf(this.need[i], this.numResources);
        }
        return copy;
    }

    /**
     * Genera una instancia con el ejemplo clásico de libros de Sistemas Operativos
     * (Silberschatz / Tanenbaum): 5 procesos (P0-P4) y 3 recursos (R0-R2).
     */
    public static BankerModel createClassicExample() {
        BankerModel model = new BankerModel(5, 3);

        // Vector Available: [3, 3, 2]
        model.available = new int[]{3, 3, 2};

        // Matriz Allocation
        model.allocation = new int[][]{
            {0, 1, 0}, // P0
            {2, 0, 0}, // P1
            {3, 0, 2}, // P2
            {2, 1, 1}, // P3
            {0, 0, 2}  // P4
        };

        // Matriz Max
        model.max = new int[][]{
            {7, 5, 3}, // P0
            {3, 2, 2}, // P1
            {9, 0, 2}, // P2
            {2, 2, 2}, // P3
            {4, 3, 3}  // P4
        };

        // Calcular matriz Need
        model.calculateNeed();

        return model;
    }

    // ================= GETTERS Y SETTERS =================

    public int getNumProcesses() {
        return numProcesses;
    }

    public int getNumResources() {
        return numResources;
    }

    public int[][] getAllocation() {
        return allocation;
    }

    public void setAllocation(int[][] allocation) {
        this.allocation = allocation;
    }

    public int[][] getMax() {
        return max;
    }

    public void setMax(int[][] max) {
        this.max = max;
    }

    public int[][] getNeed() {
        return need;
    }

    public void setNeed(int[][] need) {
        this.need = need;
    }

    public int[] getAvailable() {
        return available;
    }

    public void setAvailable(int[] available) {
        this.available = available;
    }
}
