package com.aevasquez.msvc.clients.exceptions;

import com.aevasquez.msvc.clients.dto.FieldErrorResponse;

import java.util.List;

public class DataIntegrityViolationException extends RuntimeException{

    private final int status;
    private final List<FieldErrorResponse> fields;

    public DataIntegrityViolationException(String message, int status, List<FieldErrorResponse> fields) {
        super(message);
        this.status = status;
        this.fields = fields;
    }

    public int getStatus() {
        return status;
    }

    public List<FieldErrorResponse> getFields(){
        return fields;
    }
}
