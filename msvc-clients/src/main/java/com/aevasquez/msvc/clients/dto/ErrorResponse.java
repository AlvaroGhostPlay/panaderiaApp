package com.aevasquez.msvc.clients.dto;

public record ErrorResponse(
        int status,
        String message
) {}
