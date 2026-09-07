package com.example.authservice.dto;

public record LoginResponse(
        Boolean authenticated,
        Boolean passwordChangeRequired,
        String code
) {
}
