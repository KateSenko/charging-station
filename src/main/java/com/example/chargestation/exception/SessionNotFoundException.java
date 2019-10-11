package com.example.chargestation.exception;

/**
 * This exception indicates that charge session with provided id is not found
 */
public class SessionNotFoundException extends Exception {

    public SessionNotFoundException(String stationId) {
        super(buildMessage(stationId));
    }

    private static String buildMessage(String stationId) {
        return String.format("Charge session with id [%s] is not found", stationId);
    }
}
