package com.banquero;

import com.banquero.algorithm.BankerAlgorithm;
import com.banquero.model.BankerModel;
import com.banquero.model.RequestResult;
import com.banquero.model.SafetyResult;

/**
 * Pruebas unitarias de verificación matemática para el Algoritmo del Banquero.
 */
public class TestAlgorithm {
    public static void main(String[] args) {
        System.out.println("=== TEST 1: EJEMPLO CLASICO (5 procesos, 3 recursos) ===");
        BankerModel model = BankerModel.createClassicExample();
        SafetyResult safety = BankerAlgorithm.checkSafety(model);

        System.out.println("¿Es seguro?: " + safety.isSafe());
        System.out.println("Secuencia segura: " + safety.getFormattedSequence());

        if (!safety.isSafe()) {
            throw new RuntimeException("ERROR: El ejemplo clásico debe ser seguro!");
        }

        System.out.println("\n=== TEST 2: SOLICITUD APROBADA (P1 solicita [1, 0, 2]) ===");
        RequestResult req1 = BankerAlgorithm.evaluateRequest(model, 1, new int[]{1, 0, 2});
        System.out.println("Aprobada: " + req1.isApproved());
        System.out.println("Mensaje: " + req1.getMessage());
        if (!req1.isApproved()) {
            throw new RuntimeException("ERROR: Solicitud P1 [1, 0, 2] debe ser aprobada!");
        }

        System.out.println("\n=== TEST 3: SOLICITUD EXCESIVA (P1 solicita [3, 3, 0] > Need [1, 2, 2]) ===");
        RequestResult req2 = BankerAlgorithm.evaluateRequest(model, 1, new int[]{3, 3, 0});
        System.out.println("Aprobada: " + req2.isApproved() + " (Esperado: false)");
        System.out.println("Mensaje: " + req2.getMessage());
        if (req2.isApproved()) {
            throw new RuntimeException("ERROR: Solicitud que supera Need debe ser rechazada!");
        }

        System.out.println("\n=== TEST 4: SOLICITUD INSEGURA (P4 solicita [3, 3, 0]) ===");
        RequestResult req4 = BankerAlgorithm.evaluateRequest(model, 4, new int[]{3, 3, 0});
        System.out.println("Aprobada: " + req4.isApproved() + " (Esperado: false)");
        System.out.println("Mensaje: " + req4.getMessage());
        if (req4.isApproved()) {
            throw new RuntimeException("ERROR: Solicitud que lleva a estado inseguro debe ser DENEGADA!");
        }

        System.out.println("\n========================================================");
        System.out.println("   TODAS LAS PRUEBAS MATEMATICAS PASARON CON EXITO      ");
        System.out.println("========================================================");
    }
}
