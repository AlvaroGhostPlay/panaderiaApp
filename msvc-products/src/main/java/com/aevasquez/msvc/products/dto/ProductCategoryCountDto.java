package com.aevasquez.msvc.products.dto;

public record ProductCategoryCountDto(
        String  productCategoryId,
        String typeName,
        Long productCount
) {
}
