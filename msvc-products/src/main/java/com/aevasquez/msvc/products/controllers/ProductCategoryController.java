package com.aevasquez.msvc.products.controllers;

import com.aevasquez.msvc.products.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category/private")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping()
    public ResponseEntity<?> getCategories(){
        return ResponseEntity.ok().body(productCategoryService.getAllProductCategories());
    }
}
