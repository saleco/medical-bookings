package com.github.saleco.medicalbookings.utils;

public class MedicalBookingsResponse {

    private Integer statusCode;
    private String message;

    public MedicalBookingsResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

}
