package com.app.models;

import java.util.Date;

/**
 * Represents individual menu items within an order (maps to 'order_items' table).
 */
public class OrderItem {

    private int orderItemId;      // Primary Key (unique ID for each order item)
    private int orderId;          // FK → orders.order_id
    private int menuItemId;       // FK → menu_items.menu_item_id
    private int quantity;         // Number of units ordered
    private double priceAtOrder;  // Item price at order time (stored for record)
    private Date createdAt;       // Timestamp when record was created

    // Default constructor
    public OrderItem() {}

    // Parameterized constructor
    public OrderItem(int orderItemId, int orderId, int menuItemId, int quantity, double priceAtOrder, Date createdAt) {
        super(); // Calls Object() constructor — optional but good for clarity
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void setPriceAtOrder(double priceAtOrder) {
        this.priceAtOrder = priceAtOrder;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // For logging or debugging
    @Override
    public String toString() {
        return "OrderItem [orderItemId=" + orderItemId + ", orderId=" + orderId + ", menuItemId=" + menuItemId
                + ", quantity=" + quantity + ", priceAtOrder=" + priceAtOrder + ", createdAt=" + createdAt + "]";
    }
}
