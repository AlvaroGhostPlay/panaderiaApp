package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.dto.ProductResponseDto;
import com.aevasquez.msvc.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    Page<ProductResponseDto> getAllProductPage(Pageable pageable, String categoria, UUID userId);
    Page<ProductResponseDto> getAllProductFavoritesPage(Pageable pageable, String categoria, UUID userId);
    Page<ProductResponseDto> addOrRemoveFavoriteProductByUser(UUID productId, UUID userId, String categoria, Pageable pageable);
    Page<ProductResponseDto> addOrRemoveFavoriteProductByUserFromFavorites(UUID productId, UUID userId, String categoria, Pageable pageable);
    List<ProductResponseDto> getAllProductsByIds(List<UUID> ids, UUID userId);
}
