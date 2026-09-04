package com.aevasquez.msvc.messaging.dto;

import java.util.Map;

public record EmailRequest(
        String to,
        String subject,
        String template,
        Map<String, Object> data
) {
}
