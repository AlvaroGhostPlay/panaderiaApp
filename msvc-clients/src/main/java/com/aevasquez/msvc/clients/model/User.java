package com.aevasquez.msvc.clients.model;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record User (
        UUID userId,
        String username,
        Date created,
        Date updated,
        Boolean changePass,
        Boolean enable,
        String actiovationCode,
        Set<Role> roles
) {}
