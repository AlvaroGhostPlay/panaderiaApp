package com.aevasquez.msvc.clients.dto;

import java.util.Date;
import java.util.UUID;

public record UserCreateDto(
        UUID userId,
        String username,
        String password,
        Date created,
        Date updated,
        Boolean changePass,
        Boolean enable,
        String[] roles
) {
}
