package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "msvc-products")
public interface ProductServiceFeign {

    @PostMapping("/product/private/getProductsByIds")
    ResponseEntity<List<ProductResponseDto>> getProductsByIds(@RequestBody List<UUID> ids);
}
