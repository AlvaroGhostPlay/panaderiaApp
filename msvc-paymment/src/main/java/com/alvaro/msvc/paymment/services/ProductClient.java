package com.alvaro.msvc.paymment.services;

import com.alvaro.msvc.paymment.dto.ProductResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class ProductClient {

    private final WebClient webClient;

    @Value("${services.products.url}")
    private String productsServiceUrl;

    public ProductClient(@LoadBalanced WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public ProductResponseDto findById(UUID productId) {

        return webClient
                .get()
                .uri(productsServiceUrl + "/products/{id}", productId)
                .retrieve()
                .bodyToMono(ProductResponseDto.class)
                .block();
    }

    public List<ProductResponseDto> findByIds(List<UUID> productIds) {

        return webClient
                .post()
                .uri(productsServiceUrl + "/product/private/getProductsByIds")
                .bodyValue(productIds)
                .retrieve()
                .bodyToFlux(ProductResponseDto.class)
                .collectList()
                .block();
    }
}