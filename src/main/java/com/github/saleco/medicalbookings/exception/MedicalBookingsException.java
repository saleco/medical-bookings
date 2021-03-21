package com.github.saleco.medicalbookings.exception;


public interface MedicalBookingsException {
    String getErrorCode();

    String getErrorSerial();

    default String getSupportReference() {
        return this.getErrorCode() + "-" + this.getErrorSerial();
    }

    String getMessage();

}
