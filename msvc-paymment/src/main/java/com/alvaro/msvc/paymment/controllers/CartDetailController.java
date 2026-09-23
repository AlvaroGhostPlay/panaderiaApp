package com.alvaro.msvc.paymment.controllers;

import com.alvaro.msvc.paymment.dto.UpdateCartDetail;
import com.alvaro.msvc.paymment.services.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart/detail")
public class CartDetailController {

    @Autowired
    private CartDetailService cartDetailService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping
    public ResponseEntity<?> updateCart(@RequestBody UpdateCartDetail request){
        return ResponseEntity.ok().body(cartDetailService.updateCartDetail(request));
    }
}
