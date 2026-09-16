package com.aevasquez.msvc.products.repositories;

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
    SELECT p
    FROM Product p
    JOIN p.productCategory pc
    WHERE pc.productCategoryId = :categoria
    """)
    Page<Product> findAllByProductCategoryByProductId(
            @Param("categoria") String categoria,
            Pageable pageable
    );

    List<Product> findAllByProductIdIn(List<UUID> ids);

}
