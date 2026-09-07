package com.example.authservice.dto;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedUserResponse(
        Boolean authenticated,
        UUID userId,
        String username,
        Boolean enabled,
        Boolean changePass,
        Set<String> roles
) {
}
