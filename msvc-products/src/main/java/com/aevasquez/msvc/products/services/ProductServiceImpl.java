package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.model.Product;
import com.aevasquez.msvc.products.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageService imageService;

    @Override
    //@Transactional
    public Page<Product> getAllProductPage(Pageable pageable, String categoria) {
        Page<Product> productPage = productRepository.findAllByProductCategoryByProductId(categoria, pageable);
        Map<String, String> imagesMap = productPage
                .stream()
                .collect(Collectors.toMap(
                        product -> product.getProductId().toString(),
                        product -> product.getImageUrl()
                ));

        imagesMap = this.imageService.getImagesByMap(imagesMap);

        Map<String, String> finalImagesMap = imagesMap;
        return productPage.map(product -> {
            String image = finalImagesMap.get(product.getProductId().toString());
            product.setImageUrl(image);
            return product;
        });
    }
}
