package com.aevasquez.msvc.products.dto;

import com.aevasquez.msvc.products.model.Product;

public interface ProductFavoriteProjection {

    Product getProduct();

    Boolean getFavorite();
}
