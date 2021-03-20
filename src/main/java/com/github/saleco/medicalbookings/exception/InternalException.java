package com.github.saleco.medicalbookings.exception;

public class InternalException extends TechnicalException {
    private static final String ERROR_CODE = "internal";

    public InternalException(Throwable cause) {
        super(ERROR_CODE, cause);
    }
}
