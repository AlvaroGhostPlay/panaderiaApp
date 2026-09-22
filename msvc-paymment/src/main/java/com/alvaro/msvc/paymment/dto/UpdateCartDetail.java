package com.alvaro.msvc.paymment.dto;

import java.util.UUID;

public record UpdateCartDetail(
        CartDetailAcction cartDetailAcction,
        UUID cartDetailId
) {
}
