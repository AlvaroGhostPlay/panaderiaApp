package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.dto.ProductCategoryCountDto;
import com.aevasquez.msvc.products.model.ProductCategory;

import java.util.List;

public interface ProductCategoryService {

    List<ProductCategory> getAllCategories();

    List<ProductCategoryCountDto> getAllProductCategories();
}
