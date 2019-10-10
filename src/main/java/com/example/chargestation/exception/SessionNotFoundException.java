package com.example.chargestation.exception;

/**
 * This exception indicates that charge session with provided id is not found
 */
public class SessionNotFoundException extends Exception {

    /**
     * Instantiates a new {@link SessionNotFoundException} object.
     *
     * @param stationId the user unique identifier
     */
    public SessionNotFoundException(String stationId) {
        super(buildMessage(stationId));
    }

    /**
     * Builds message string.
     *
     * @param stationId the station unique identifier
     * @return the string that describes exception
     */
    static String buildMessage(String stationId) {
        return String.format("Charge session with id [%s] is not found", stationId);
    }
}
