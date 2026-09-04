package com.aevasquez.msvc.messaging.services;

import com.aevasquez.msvc.messaging.dto.ImageUploadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "images-service")
public interface ImageService {

    @GetMapping("/public/images")
    ImageUploadResponse getImagePublic(@RequestParam String filename);
}
