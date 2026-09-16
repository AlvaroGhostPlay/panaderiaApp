package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.dto.ProductCategoryCountDto;
import com.aevasquez.msvc.products.model.ProductCategory;
import com.aevasquez.msvc.products.repositories.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public List<ProductCategoryCountDto> getAllProductCategories() {
        return productCategoryRepository.findAllWithProductCount();
    }
}
