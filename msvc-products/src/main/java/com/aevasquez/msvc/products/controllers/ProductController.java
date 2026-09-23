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
            @RequestParam String categoria,
            @RequestParam UUID userId){

        Pageable pageable = PageRequest.of(page, cantidad);
        return ResponseEntity.ok().body(productService.getAllProductPage(pageable, categoria, userId));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/getProductsFavorites")
    public ResponseEntity<?> getProductsFavorites(
            @RequestParam UUID userId,
            @RequestParam Integer page,
            @RequestParam Integer cantidad,
            @RequestParam String categoria){
        Pageable pageable = PageRequest.of(page, cantidad);
        return ResponseEntity.ok().body(productService.getAllProductFavoritesPage(pageable, categoria, userId));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/addOrRemoveProductFavoriteByUser")
    public ResponseEntity<?> addOrRemoveProductFavoriteByUser(
            @RequestParam UUID productId,
            @RequestParam UUID userId,
            @RequestParam Integer page,
            @RequestParam Integer cantidad,
            @RequestParam String categoria){
        Pageable pageable = PageRequest.of(page, cantidad);
        return ResponseEntity.ok().body(productService.addOrRemoveFavoriteProductByUser(productId, userId, categoria, pageable));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/addOrRemoveProductFavoriteByUserFromFavorites")
    public ResponseEntity<?> addOrRemoveProductFavoriteByUserFromFavorites(
            @RequestParam UUID productId,
            @RequestParam UUID userId,
            @RequestParam Integer page,
            @RequestParam Integer cantidad,
            @RequestParam String categoria){
        Pageable pageable = PageRequest.of(page, cantidad);
        return ResponseEntity.ok().body(productService.addOrRemoveFavoriteProductByUserFromFavorites(productId, userId, categoria, pageable));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/getProductsByIds")
    public ResponseEntity<?> getProductsByIds(@RequestBody List<UUID> ids, @RequestParam UUID userId){
        return ResponseEntity.ok().body(productService.getAllProductsByIds(ids,  userId));
    }

}
