package com.aevasquez.msvc.products.dto;

import java.util.Set;
import java.util.UUID;

public record ProductResponseDto(
        UUID productId,
        String productName,
        Float price,
        Boolean offer,
        String imageUrl,
        Boolean favorite,
        Set<ProductCategoryResponseDto> productCategory
) {
}
