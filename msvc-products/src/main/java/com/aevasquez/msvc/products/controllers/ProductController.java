package com.aevasquez.msvc.products.controllers;

import com.aevasquez.msvc.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/private")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/getProductStore")
    private ResponseEntity<?> getProductsPage(
            @RequestParam Integer page,
            @RequestParam Integer cantidad,
            @RequestParam String categoria){

        Pageable pageable = PageRequest.of(page, cantidad);
        return ResponseEntity.ok().body(productService.getAllProductPage(pageable, categoria));
    }
}
