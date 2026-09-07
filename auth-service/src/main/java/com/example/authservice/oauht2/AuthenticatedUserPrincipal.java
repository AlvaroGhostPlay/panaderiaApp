package com.example.authservice.oauht2;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;
import java.util.UUID;

public record AuthenticatedUserPrincipal(
        UUID userId,
        String username,
        Boolean passwordChangeRequired,
        Set<String> roles
) implements Principal, Serializable {

    @Override
    public String getName() {
        return userId.toString();
    }
}
