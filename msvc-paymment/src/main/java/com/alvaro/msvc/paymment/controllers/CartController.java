package com.alvaro.msvc.paymment.controllers;

import com.alvaro.msvc.paymment.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping()
    public ResponseEntity<?> getCart(@RequestParam UUID userId){
        return ResponseEntity.ok().body(cartService.getCartDetail(userId));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<?> createOrAddCart(@RequestParam UUID productId, @RequestParam UUID userId){
        return ResponseEntity.ok().body(cartService.createCartOrAddCart(productId, userId));
    }
}
