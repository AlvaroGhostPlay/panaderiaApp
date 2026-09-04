package com.aevasquez.msvc.clients.exceptions;

public class NotFoundException extends RuntimeException{
    private final int status;

    public NotFoundException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
