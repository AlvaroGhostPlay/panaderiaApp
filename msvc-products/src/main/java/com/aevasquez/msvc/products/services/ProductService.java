package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<Product> getAllProductPage(Pageable pageable, String categoria);
}
