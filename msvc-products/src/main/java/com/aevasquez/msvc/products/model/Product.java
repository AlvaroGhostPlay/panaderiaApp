package com.aevasquez.msvc.products.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "storedb")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID productId;

    private String productName;
    private Float price;
    private Boolean offer;
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "products_categories_map",
            schema = "storedb",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "product_category_id ")
    )
    private Set<ProductCategory> productCategory;

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getOffer() {
        return offer;
    }

    public void setOffer(Boolean offer) {
        this.offer = offer;
    }

    public Set<ProductCategory> getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Set<ProductCategory> productCategory) {
        this.productCategory = productCategory;
    }
}
