package com.github.saleco.medicalbookings.exception;


public interface MedicalBookingsException {
    String getErrorCode();

    String getErrorSerial();

    default String getSupportReference() {
        return this.getErrorCode() + "-" + this.getErrorSerial();
    }

    String getMessage();

    Throwable getCause();

    Throwable asThrowable();

    static MedicalBookingsException getCommandException(Throwable t) {
        if (t == null) {
            return null;
        } else if (t instanceof MedicalBookingsException) {
            return (MedicalBookingsException)t;
        } else {
            for(Throwable cause = t.getCause(); cause != null; cause = cause.getCause()) {
                if (cause instanceof MedicalBookingsException) {
                    return (MedicalBookingsException)cause;
                }
            }

            return new InternalException(t);
        }
    }
}
