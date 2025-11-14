package com.app.models;

import java.sql.Timestamp;

/**
 * ✅ Represents the 'carts' table from the database.
 * One active cart per user.
 */
public class Cart {
    
    private int cartId;          // Primary key
    private int userId;          // Foreign key referencing users(user_id)
    private Timestamp createdAt; // Timestamp of creation
    private Timestamp updatedAt; // Timestamp of last update

    // ---------------- Constructors ----------------
    public Cart() {}

    public Cart(int cartId, int userId, Timestamp createdAt, Timestamp updatedAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ---------------- Getters & Setters ----------------
    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
