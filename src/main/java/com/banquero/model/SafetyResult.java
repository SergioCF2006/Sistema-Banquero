package com.banquero.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el resultado del algoritmo de seguridad del Banquero.
 * Almacena si el sistema es seguro, la secuencia segura (si existe),
 * y un registro detallado paso a paso para fines pedagógicos.
 */
public class SafetyResult {

    private final boolean safe;
    private final List<Integer> safeSequence;
    private final List<String> logSteps;
    private final List<Integer> unfinishedProcesses;

    public SafetyResult(boolean safe, List<Integer> safeSequence, List<String> logSteps, List<Integer> unfinishedProcesses) {
        this.safe = safe;
        this.safeSequence = safeSequence != null ? new ArrayList<>(safeSequence) : Collections.emptyList();
        this.logSteps = logSteps != null ? new ArrayList<>(logSteps) : Collections.emptyList();
        this.unfinishedProcesses = unfinishedProcesses != null ? new ArrayList<>(unfinishedProcesses) : Collections.emptyList();
    }

    public boolean isSafe() {
        return safe;
    }

    public List<Integer> getSafeSequence() {
        return Collections.unmodifiableList(safeSequence);
    }

    public List<String> getLogSteps() {
        return Collections.unmodifiableList(logSteps);
    }

    public List<Integer> getUnfinishedProcesses() {
        return Collections.unmodifiableList(unfinishedProcesses);
    }

    /**
     * Retorna la secuencia segura con formato amigable, ej. "P1 → P3 → P4 → P0 → P2".
     */
    public String getFormattedSequence() {
        if (!safe || safeSequence.isEmpty()) {
            return "Ninguna (Estado Inseguro)";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < safeSequence.size(); i++) {
            sb.append("P").append(safeSequence.get(i));
            if (i < safeSequence.size() - 1) {
                sb.append(" → ");
            }
        }
        return sb.toString();
    }
}
