package com.aevasquez.msvc.clients.dto;

import java.util.List;

public record ErrorResponseList(
        int status,
        String message,
        List<FieldErrorResponse> errors
) {
}
