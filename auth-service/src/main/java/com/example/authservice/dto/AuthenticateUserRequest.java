package com.example.authservice.dto;

public record AuthenticateUserRequest(
        String username,
        String password
) {
}
