package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.CartResponseDto;
import com.alvaro.msvc.paymment.dto.UpdateCartDetail;

public interface CartDetailService {

    CartResponseDto updateCartDetail(UpdateCartDetail request);
}
