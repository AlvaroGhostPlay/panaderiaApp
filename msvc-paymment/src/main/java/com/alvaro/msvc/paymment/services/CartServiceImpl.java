package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.CartResponseDto;
import com.alvaro.msvc.paymment.dto.CreateCartRequestDto;
import com.alvaro.msvc.paymment.exceptions.NotFoundException;
import com.alvaro.msvc.paymment.mapper.MapperCart;
import com.alvaro.msvc.paymment.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MapperCart mapperCart;


    @Override
    @Transactional
    public CartResponseDto getAllDetail() {
        return null;
    }

    @Override
    @Transactional
    public CartResponseDto getCartDetail(UUID userId) {
        System.out.println(cartRepository.findByUserIdAndStateIsFalse(userId));
        return cartRepository.findByUserIdAndStateIsFalse(userId)
                .map(cart -> mapperCart.toCartResponseDto(cart))
                .orElseThrow(() -> new NotFoundException("No se encuentra carrito disponible, debe de crear uno", HttpStatus.NOT_FOUND.value()));
    }

    @Override
    @Transactional
    public CartResponseDto createCart(CreateCartRequestDto request) {
        return null;
    }
}
