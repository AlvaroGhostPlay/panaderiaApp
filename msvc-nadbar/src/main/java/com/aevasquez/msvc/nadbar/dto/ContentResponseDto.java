package com.aevasquez.msvc.nadbar.dto;

import java.util.UUID;

public record ContentResponseDto(
        UUID contentId,
        String identifier,
        String content,
        String type
) {
}
