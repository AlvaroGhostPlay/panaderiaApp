package com.aevasquez.msvc.products.repositories;

import com.aevasquez.msvc.products.model.FavoriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, UUID> {
    Optional<FavoriteProduct> findByUserIdAndProductId(UUID userId, UUID productId);
}
