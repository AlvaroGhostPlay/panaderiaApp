package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.dto.ProductCategoryResponseDto;
import com.aevasquez.msvc.products.dto.ProductResponseDto;
import com.aevasquez.msvc.products.model.Product;
import com.aevasquez.msvc.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    @Override
    //@Transactional
    public Page<ProductResponseDto> getAllProductPage(
            Pageable pageable,
            String categoria
    ) {

        System.out.println("============================");
        System.out.println("CATEGORIA = " + categoria);
        System.out.println("PAGE      = " + pageable.getPageNumber());
        System.out.println("SIZE      = " + pageable.getPageSize());

        Page<Product> productPage =
                productRepository.findAllByProductCategoryByProductId(
                        categoria,
                        pageable
                );

        System.out.println("CONTENT SIZE   = "
                + productPage.getContent().size());

        System.out.println("TOTAL ELEMENTS = "
                + productPage.getTotalElements());

        System.out.println("TOTAL PAGES    = "
                + productPage.getTotalPages());

        productPage.getContent().forEach(product ->
                System.out.println(
                        "PRODUCTO = "
                                + product.getProductId()
                                + " | "
                                + product.getProductName()
                )
        );

        System.out.println("============================");

        Map<String, String> imagesMap = productPage.stream()
                .collect(Collectors.toMap(
                        product -> product.getProductId().toString(),
                        Product::getImageUrl
                ));

        imagesMap = imageService.getImagesByMap(imagesMap);

        Map<String, String> finalImagesMap = imagesMap;

        return productPage.map(product -> {
            product.setImageUrl(
                    finalImagesMap.get(
                            product.getProductId().toString()
                    )
            );
            return new ProductResponseDto(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getOffer(),
                    product.getImageUrl(),
                    product.getProductCategory()
                            .stream()
                            .map(category -> new ProductCategoryResponseDto(category.getProductCategoryId(), category.getTypeName()))
                            .collect(Collectors.toSet())
                    );
        });
    }

    @Override
    public List<ProductResponseDto> getAllProductsByIds(List<UUID> ids) {
        List<Product> products = productRepository.findAllByProductIdIn(ids);
        Map<String, String> imagesMap = products
                .stream()
                .collect(Collectors.toMap(
                        product -> product.getProductId().toString(),
                        product -> product.getImageUrl()
                ));

        imagesMap = this.imageService.getImagesByMap(imagesMap);

        Map<String, String> finalImagesMap = imagesMap;
        return products
                .stream()
                .map(product -> {
            String image = finalImagesMap.get(product.getProductId().toString());
            product.setImageUrl(image);
            return new ProductResponseDto(
                    product.getProductId(),
                    product.getProductName(),
                    product.getPrice(),
                    product.getOffer(),
                    product.getImageUrl(),
                    product.getProductCategory()
                            .stream()
                            .map(category -> new ProductCategoryResponseDto(category.getProductCategoryId(), category.getTypeName()))
                            .collect(Collectors.toSet())
            );
        }).toList();
    }
}
