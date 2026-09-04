package com.aevasquez.msvc.clients.dto;

public record FieldErrorResponse(
        String campo,
        String mensaje
) {
}
