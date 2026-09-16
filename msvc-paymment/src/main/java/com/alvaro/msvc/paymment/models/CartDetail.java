package com.alvaro.msvc.paymment.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "cart_details", schema = "storedb")
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartDetailId;
    private UUID productId;
    private Integer quantity;
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public UUID getCartDetailId() {
        return cartDetailId;
    }

    public void setCartDetailId(UUID cartDetailId) {
        this.cartDetailId = cartDetailId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
