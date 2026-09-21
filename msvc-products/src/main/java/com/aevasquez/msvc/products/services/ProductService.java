package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.dto.ProductResponseDto;
import com.aevasquez.msvc.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductResponseDto> getAllProductPage(Pageable pageable, String categoria);
    List<ProductResponseDto> getAllProductsByIds(List<UUID> ids);
}
