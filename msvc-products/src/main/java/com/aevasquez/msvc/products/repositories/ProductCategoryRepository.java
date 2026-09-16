package com.aevasquez.msvc.products.repositories;

import com.aevasquez.msvc.products.dto.ProductCategoryCountDto;
import com.aevasquez.msvc.products.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {

    @Query("""
        SELECT new com.aevasquez.msvc.products.dto.ProductCategoryCountDto(
            pc.productCategoryId,
            pc.typeName,
            COUNT(p)
        )
        FROM ProductCategory pc
        LEFT JOIN pc.products p
        GROUP BY pc.productCategoryId, pc.typeName
        ORDER BY pc.typeName
    """)
    List<ProductCategoryCountDto> findAllWithProductCount();
}
