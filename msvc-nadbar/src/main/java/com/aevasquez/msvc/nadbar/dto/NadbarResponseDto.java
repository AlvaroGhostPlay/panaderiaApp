package com.aevasquez.msvc.nadbar.dto;

import java.util.UUID;

public record NadbarResponseDto (
        UUID idNadbar,
        String typeNadbar,
        String path,
        String title
){
}
