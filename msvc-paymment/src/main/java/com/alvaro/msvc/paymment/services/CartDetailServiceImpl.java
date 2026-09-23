package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.CartDetailAcction;
import com.alvaro.msvc.paymment.dto.CartResponseDto;
import com.alvaro.msvc.paymment.dto.UpdateCartDetail;
import com.alvaro.msvc.paymment.exceptions.NotFoundException;
import com.alvaro.msvc.paymment.mapper.MapperCart;
import com.alvaro.msvc.paymment.models.Cart;
import com.alvaro.msvc.paymment.models.CartDetail;
import com.alvaro.msvc.paymment.repositories.CartDetialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartDetailServiceImpl implements CartDetailService {

    @Autowired
    private CartDetialRepository cartDetialRepository;

    @Autowired
    private MapperCart mapperCart;

    @Override
    @Transactional
    public CartResponseDto updateCartDetail(UpdateCartDetail request) {

        CartDetail cartDetailDb = cartDetialRepository.findById(request.cartDetailId())
                .orElseThrow(() -> new NotFoundException(
                        "No existe este producto en el carrito",
                        404
                ));

        Cart cart = cartDetailDb.getCart();

        if (request.cartDetailAcction() == CartDetailAcction.ADD) {
            cartDetailDb.setQuantity(cartDetailDb.getQuantity() +1);
            cartDetialRepository.save(cartDetailDb);
        } else {
            if (cartDetailDb.getQuantity() == 1){
                cartDetialRepository.delete(cartDetailDb);
            } else {
                cartDetailDb.setQuantity(cartDetailDb.getQuantity() -1);
                cartDetialRepository.save(cartDetailDb);
            }
        }
        return mapperCart.toCartResponseDto(cart, cart.getUserId());
    }
}
