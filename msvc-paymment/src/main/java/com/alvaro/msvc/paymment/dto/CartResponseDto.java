package com.alvaro.msvc.paymment.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CartResponseDto(
        UUID cartId,
        UUID userId,
        Boolean state,
        Date updated,
        Integer totalItems,
        List<CartDetailResponseDto> cartDetailResponseDto
) {
}
