package com.aevasquez.msvc.products.controllers;

import com.aevasquez.msvc.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product/private")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/getProductStore")
    public ResponseEntity<?> getProductsPage(
            @RequestParam Integer page,
            @RequestParam Integer cantidad,
            @RequestParam String categoria){

        Pageable pageable = PageRequest.of(page, cantidad);
        return ResponseEntity.ok().body(productService.getAllProductPage(pageable, categoria));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/getProductsByIds")
    public ResponseEntity<?> getProductsByIds(@RequestBody List<UUID> ids){
        return ResponseEntity.ok().body(productService.getAllProductsByIds(ids));
    }

}
