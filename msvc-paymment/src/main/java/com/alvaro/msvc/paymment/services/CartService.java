package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.CartResponseDto;

import java.util.UUID;

public interface CartService {

    CartResponseDto getAllDetail();
    CartResponseDto getCartDetail(UUID userId);
    CartResponseDto createCartOrAddCart(UUID productId, UUID userId);

}
