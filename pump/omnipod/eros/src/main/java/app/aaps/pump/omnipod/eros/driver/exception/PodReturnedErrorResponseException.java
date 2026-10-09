package app.gmaaps.pump.omnipod.eros.driver.exception;

import app.gmaaps.pump.omnipod.eros.driver.communication.message.response.ErrorResponse;

public class PodReturnedErrorResponseException extends OmnipodException {
    private final ErrorResponse errorResponse;

    public PodReturnedErrorResponseException(ErrorResponse errorResponse) {
        super("Pod returned error response: " + errorResponse, true);
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}

