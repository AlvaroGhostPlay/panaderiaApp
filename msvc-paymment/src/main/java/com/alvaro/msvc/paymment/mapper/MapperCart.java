package com.alvaro.msvc.paymment.mapper;

import com.alvaro.msvc.paymment.dto.CartDetailResponseDto;
import com.alvaro.msvc.paymment.dto.CartResponseDto;
import com.alvaro.msvc.paymment.dto.ProductResponseDto;
import com.alvaro.msvc.paymment.models.Cart;
import com.alvaro.msvc.paymment.models.CartDetail;
import com.alvaro.msvc.paymment.services.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MapperCart {

    @Autowired
    private ProductClient productClient;

    public CartResponseDto toCartResponseDto(Cart cart) {
        Integer cantidad = cart.getCartDetails().size();
        return  new CartResponseDto(
                cart.getCartId(),
                cart.getUserId(),
                cart.getState(),
                cart.getUpdated(),
                cantidad,
                this.toCartDetails(cart.getCartDetails())
        );
    }

    public List<CartDetailResponseDto> toCartDetails(List<CartDetail> cartDetails) {
        List<UUID> productIds = cartDetails
                .stream()
                .map(cartDetail -> cartDetail.getProductId())
                .toList();

        List<ProductResponseDto> productResponse = this.productClient.findByIds(productIds);

        Map<UUID, ProductResponseDto> productsById = productResponse.stream()
                .collect(Collectors.toMap(
                        ProductResponseDto::productId,
                        Function.identity()
                ));

        return  cartDetails
                .stream()
                .map(cartDetail -> this.toCartDetailResponseDto(cartDetail, productsById.get(cartDetail.getProductId())))
                .toList();
    }

    public CartDetailResponseDto toCartDetailResponseDto(CartDetail cartDetail, ProductResponseDto productResponse ) {

        return  new CartDetailResponseDto(
                cartDetail.getCartDetailId(),
                productResponse,
                cartDetail.getQuantity(),
                cartDetail.getUpdated()
        );
    }
}
