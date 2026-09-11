package com.aevasquez.msvc.products.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "images-service")
public interface ImageService {

    @PostMapping("/private/products-image-map")
    Map<String, String> getImagesByMap(@RequestBody Map<String, String> images);
}
