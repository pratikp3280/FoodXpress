package com.app.models;

import java.util.Date;

/**
 * Represents a single item in a user's cart (maps to 'cart_items' table).
 */
public class CartItem {

    // Unique ID for each cart item (Primary Key)
    private int cartItemId;

    // ID of the cart this item belongs to (Foreign Key)
    private int cartId;

    // ID of the menu item (Foreign Key)
    private int menuItemId;

    // Quantity of this menu item in the cart
    private int quantity;

    // Timestamp when the item was added
    private Date addedAt;

    // Default constructor
    public CartItem() {}

    // Parameterized constructor
    public CartItem(int cartItemId, int cartId, int menuItemId, int quantity, Date addedAt) {
        this.cartItemId = cartItemId;
        this.cartId = cartId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.addedAt = addedAt;
    }

    // Getters and Setters
    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Date addedAt) {
        this.addedAt = addedAt;
    }

    // For debugging and logging
    @Override
    public String toString() {
        return "CartItem [cartItemId=" + cartItemId + ", cartId=" + cartId + ", menuItemId=" + menuItemId
                + ", quantity=" + quantity + ", addedAt=" + addedAt + "]";
    }
}
