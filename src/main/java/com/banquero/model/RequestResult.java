package com.banquero.model;

/**
 * Encapsula el resultado de evaluar una Solicitud de Recursos (Resource Request Algorithm).
 */
public class RequestResult {

    private final boolean approved;
    private final String message;
    private final String detailLog;
    private final SafetyResult safetyResult;
    private final BankerModel simulatedModel;

    public RequestResult(boolean approved, String message, String detailLog, 
                         SafetyResult safetyResult, BankerModel simulatedModel) {
        this.approved = approved;
        this.message = message;
        this.detailLog = detailLog;
        this.safetyResult = safetyResult;
        this.simulatedModel = simulatedModel;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getMessage() {
        return message;
    }

    public String getDetailLog() {
        return detailLog;
    }

    public SafetyResult getSafetyResult() {
        return safetyResult;
    }

    public BankerModel getSimulatedModel() {
        return simulatedModel;
    }
}
