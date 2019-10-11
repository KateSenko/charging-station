package com.example.chargestation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *  Charge station exception handler
 *  Handles exceptions on charge station controller and wrap it with correct http status
 */
@ControllerAdvice(basePackages = "com.example.chargestation.controller")
public class ChargeStationExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeStationExceptionHandler.class);
    private static final String ERROR_MESSAGE_PATTERN = "Error: {}";

    /**
     * Handle {@link SessionNotFoundException}.
     *
     * @param exception {@link SessionNotFoundException} object
     * @return the response entity with error message
     */
    @ExceptionHandler(value = SessionNotFoundException.class)
    public ResponseEntity<Object> handleSessionNotFoundException(SessionNotFoundException exception) {
        LOGGER.error(ERROR_MESSAGE_PATTERN, exception.getMessage());
        return new ResponseEntity<>(new RestErrorMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
