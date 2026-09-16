package com.alvaro.msvc.paymment.controllers;

import com.alvaro.msvc.paymment.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping()
    public ResponseEntity<?> getCart(@RequestParam UUID userId){
        return ResponseEntity.ok().body(cartService.getCartDetail(userId));
    }
}
