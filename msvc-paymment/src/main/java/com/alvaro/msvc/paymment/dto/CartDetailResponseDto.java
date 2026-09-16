package com.alvaro.msvc.paymment.dto;

import java.util.Date;
import java.util.UUID;

public record CartDetailResponseDto(
        UUID cartDetailId,
        ProductResponseDto  product,
        Integer quantity,
        Date updated
) {
}
