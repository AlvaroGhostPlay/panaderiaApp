package com.aevasquez.msvc.products.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "user_favorites",
        schema = "storedb",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_product_favorite",
                        columnNames = {"user_id", "product_id"}
                )
        }
)
public class FavoriteProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID favoriteId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public UUID getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(UUID favoriteId) {
        this.favoriteId = favoriteId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}