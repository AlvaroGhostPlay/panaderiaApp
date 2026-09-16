package com.alvaro.msvc.paymment.dto;

public record ErrorException(
        int status,
        String message
) {
}
