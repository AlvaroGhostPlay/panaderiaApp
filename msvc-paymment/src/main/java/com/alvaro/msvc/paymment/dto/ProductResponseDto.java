package com.alvaro.msvc.paymment.dto;

import java.util.Set;
import java.util.UUID;

public record ProductResponseDto(
        UUID productId,
        String productName,
        Float price,
        Boolean offer,
        String imageUrl,
        Set<ProductCategoryResponseDto> productCategory
) {
}
