package com.aevasquez.msvc.products.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "product_categories", schema = "storedb")
public class ProductCategory {

    @Id
    private String productCategoryId;

    private String typeName;

    @ManyToMany(mappedBy = "productCategory", fetch = FetchType.LAZY)
    private Set<Product> products;

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
