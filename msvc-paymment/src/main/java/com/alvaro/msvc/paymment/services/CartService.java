package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.CartResponseDto;
import com.alvaro.msvc.paymment.dto.CreateCartRequestDto;

import java.util.UUID;

public interface CartService {

    CartResponseDto getAllDetail();
    CartResponseDto getCartDetail(UUID userId);
    CartResponseDto createCart(CreateCartRequestDto request);

}
