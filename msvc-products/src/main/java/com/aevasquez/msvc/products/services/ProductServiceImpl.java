package com.aevasquez.msvc.products.services;

import com.aevasquez.msvc.products.dto.ProductResponseDto;
import com.aevasquez.msvc.products.mapper.ProductMapper;
import com.aevasquez.msvc.products.model.FavoriteProduct;
import com.aevasquez.msvc.products.model.Product;
import com.aevasquez.msvc.products.repositories.FavoriteProductRepository;
import com.aevasquez.msvc.products.repositories.ProductRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FavoriteProductRepository favoriteProductRepository;

    @Autowired
    private ProductMapper productMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getAllProductPage(Pageable pageable, String categoria, UUID userId) {
        return this.productMapper.createProductFavoriteProjection(categoria, userId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponseDto> getAllProductFavoritesPage(Pageable pageable, String categoria, UUID userId) {
        return this.productMapper.createProductFavoriteProjection(categoria, userId, pageable);
    }

    @Transactional
    @Override
    public Page<ProductResponseDto> addOrRemoveFavoriteProductByUser(UUID productId, UUID userId, String categoria, Pageable pageable) {
        Optional<FavoriteProduct> productFavorite = favoriteProductRepository.findByUserIdAndProductId(userId, productId);
        if (productFavorite.isPresent()) {
            favoriteProductRepository.delete(productFavorite.get());
        } else {
            Product product = this.productRepository.findById(productId).orElseThrow(() -> new NotFoundException());
            FavoriteProduct favoriteProduct = new FavoriteProduct();
            favoriteProduct.setUserId(userId);
            favoriteProduct.setProduct(product);
            favoriteProductRepository.save(favoriteProduct);
        }
        return this.productMapper.createProductFavoriteProjection(categoria, userId, pageable);
    }

    @Transactional
    @Override
    public Page<ProductResponseDto> addOrRemoveFavoriteProductByUserFromFavorites(UUID productId, UUID userId, String categoria, Pageable pageable) {
        Optional<FavoriteProduct> productFavorite = favoriteProductRepository.findByUserIdAndProductId(userId, productId);
        if (productFavorite.isPresent()) {
            favoriteProductRepository.delete(productFavorite.get());
        } else {
            Product product = this.productRepository.getReferenceById(productId);
            FavoriteProduct favoriteProduct = new FavoriteProduct();
            favoriteProduct.setUserId(userId);
            favoriteProduct.setProduct(product);
            favoriteProductRepository.save(favoriteProduct);
        }
        return this.productMapper.createProductFavoriteProjection(categoria, userId, pageable);
    }

    @Override
    public List<ProductResponseDto> getAllProductsByIds(List<UUID> ids, UUID userId) {
        return productMapper.createProductFavoriteProjection(ids, userId);
    }
}
