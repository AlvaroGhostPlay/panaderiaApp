package com.aevasquez.msvc.products.repositories;

import com.aevasquez.msvc.products.dto.ProductFavoriteProjection;
import com.aevasquez.msvc.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("""
    SELECT
        p AS product,
        CASE
            WHEN f.favoriteId IS NOT NULL THEN true
            ELSE false
        END AS favorite
    FROM Product p
    JOIN p.productCategory pc
    LEFT JOIN FavoriteProduct f
        ON f.product = p
        AND f.userId = :userId
    WHERE pc.productCategoryId = :categoria
    """)
    Page<ProductFavoriteProjection> findAllByProductCategoryByProductId(
            @Param("categoria") String categoria,
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query("""
    SELECT
        p AS product,
        true AS favorite
    FROM Product p
    JOIN p.productCategory pc
    JOIN FavoriteProduct f
        ON f.product = p
        AND f.userId = :userId
    WHERE pc.productCategoryId = :categoria
    """)
    Page<ProductFavoriteProjection> findFavoriteProductsByCategory(
            @Param("categoria") String categoria,
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query("""
    SELECT
        p AS product,
        CASE
            WHEN f.favoriteId IS NOT NULL THEN true
            ELSE false
        END AS favorite
    FROM Product p
    LEFT JOIN FavoriteProduct f
        ON f.product = p
        AND f.userId = :userId
    WHERE p.productId IN :ids
    """)
    List<ProductFavoriteProjection> findAllByProductIdIn(
            @Param("ids") List<UUID> ids,
            @Param("userId") UUID userId
    );

}
