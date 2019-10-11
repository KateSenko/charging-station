package com.example.chargestation.exception;

/**
 *  POJO for wrapping exception messages for {@link ChargeStationExceptionHandler}
 */
public class RestErrorMessage {
    private String error;

    public RestErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
