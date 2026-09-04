package com.aevasquez.msvc.nadbar.dto;

import java.util.List;

public record ListConentResponseDto(
        String contenType,
        List<ContentResponseDto> contents
) {
}
