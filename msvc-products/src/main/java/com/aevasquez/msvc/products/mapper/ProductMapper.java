package com.aevasquez.msvc.products.mapper;

import com.aevasquez.msvc.products.dto.ProductCategoryResponseDto;
import com.aevasquez.msvc.products.dto.ProductFavoriteProjection;
import com.aevasquez.msvc.products.dto.ProductResponseDto;
import com.aevasquez.msvc.products.model.Product;
import com.aevasquez.msvc.products.model.ProductCategory;
import com.aevasquez.msvc.products.repositories.ProductRepository;
import com.aevasquez.msvc.products.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    public ProductResponseDto createProductResponseDto(ProductFavoriteProjection result, Map<String, String> imagesMap) {
        Product product = result.getProduct();

        String imageUrl = imagesMap.get(
                product.getProductId().toString()
        );

        return new ProductResponseDto(
                product.getProductId(),
                product.getProductName(),
                product.getPrice(),
                product.getOffer(),
                imageUrl,
                result.getFavorite(),
                product.getProductCategory()
                        .stream()
                        .map(this::createProductCategoryResponseDto)
                        .collect(Collectors.toSet())
        );
    }

    public ProductCategoryResponseDto createProductCategoryResponseDto(ProductCategory productCategory) {
        return new ProductCategoryResponseDto(
                productCategory.getProductCategoryId(),
                productCategory.getTypeName()
        );
    }

    public Page<ProductResponseDto> createProductFavoriteProjection(String categoria, UUID userId, Pageable pageable) {
        Page<ProductFavoriteProjection> productPage = productRepository.findFavoriteProductsByCategory(categoria, userId, pageable);
        Map<String, String>  imagesMap = this.getImagesByMap(productPage);
        return productPage.map(result -> this.createProductResponseDto(result, imagesMap));
    }

    public List<ProductResponseDto> createProductFavoriteProjection(List<UUID> ids, UUID userId) {
        List<ProductFavoriteProjection> productList = productRepository.findAllByProductIdIn(ids, userId);
        Map<String, String>  imagesMap = this.getImagesByMap(productList);
        return productList
                .stream()
                .map(result -> this.createProductResponseDto(result, imagesMap))
                .toList();
    }

    private Map<String, String> getImagesByMap(Page<ProductFavoriteProjection> productPage) {
        Map<String, String> imagesMap = productPage.stream()
                .map(ProductFavoriteProjection::getProduct)
                .collect(Collectors.toMap(
                        product -> product.getProductId().toString(),
                        Product::getImageUrl
                ));
        return imageService.getImagesByMap(imagesMap);
    }

    private Map<String, String> getImagesByMap(List<ProductFavoriteProjection> products) {
        Map<String, String> imagesMap = products.stream()
                .map(ProductFavoriteProjection::getProduct)
                .collect(Collectors.toMap(
                        product -> product.getProductId().toString(),
                        Product::getImageUrl
                ));
        return imageService.getImagesByMap(imagesMap);
    }
}
