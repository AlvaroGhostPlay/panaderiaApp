package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.CartResponseDto;
import com.alvaro.msvc.paymment.exceptions.NotFoundException;
import com.alvaro.msvc.paymment.mapper.MapperCart;
import com.alvaro.msvc.paymment.models.Cart;
import com.alvaro.msvc.paymment.models.CartDetail;
import com.alvaro.msvc.paymment.repositories.CartDetialRepository;
import com.alvaro.msvc.paymment.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetialRepository cartDetialRepository;

    @Autowired
    private MapperCart mapperCart;


    @Override
    @Transactional
    public CartResponseDto getAllDetail() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getCartDetail(UUID userId) {
        return cartRepository.findByUserIdAndStateIsFalse(userId)
                .map(cart -> mapperCart.toCartResponseDto(cart, userId))
                .orElseThrow(() -> new NotFoundException("No se encuentra carrito disponible, debe de crear uno", HttpStatus.NOT_FOUND.value()));
    }

    @Override
    @Transactional
    public CartResponseDto createCartOrAddCart(UUID productId, UUID userId) {

        Cart cart = cartRepository.findByUserIdAndStateIsFalse(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setState(false);
                    newCart.setUpdated(new Date());
                    newCart.setCartDetails(new ArrayList<>());
                    return newCart;
                });

        Optional<CartDetail> cartDetailDb = cartDetialRepository.findByProductIdAndCartId(productId, cart.getCartId());

        if (cartDetailDb.isPresent()) {

            CartDetail cartDetail = cartDetailDb.get();

            cartDetail.setQuantity(cartDetail.getQuantity() + 1);
            cartDetail.setUpdated(new Date());

        } else {

            CartDetail cartDetail = new CartDetail();

            cartDetail.setProductId(productId);
            cartDetail.setQuantity(1);
            cartDetail.setUpdated(new Date());
            cartDetail.setCart(cart);

            cart.getCartDetails().add(cartDetail);
        }

        cart.setUpdated(new Date());

        cartRepository.save(cart);

        return mapperCart.toCartResponseDto(cart, userId);
    }
}
