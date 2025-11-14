package com.app.models;

import java.util.Date;

/**
 * Represents a customer's order (maps to 'orders' table in DB).
 */
public class Order {

    
    private int orderId; 		// Unique ID for each order (Primary Key)
    private int userId;   		// ID of the user who placed the order (Foreign Key)
    private int restaurantId;   // ID of the restaurant from which the order is placed (Foreign Key)
    private Integer addressId;   // Address used for delivery (can be null if not applicable)
    private double totalAmount;  // Total bill amount for the order
    private String status;       // Order current status (ENUM: placed, accepted, preparing, out_for_delivery, delivered, cancelled)
    private String paymentMethod;  // Payment method used (ENUM: cash_on_delivery, card, upi)
    private String paymentStatus;  // Payment status (ENUM: pending, completed, failed)
    private String deliveryInstructions;  // Special delivery notes (optional)
    private Date createdAt;  // Order creation timestamp
    private Date updatedAt;  // // Last updated timestamp
   
    // Default constructor (used when creating an empty object)
    public Order() {}

    /**
     * Parameterized constructor (used to initialize full order details).
     * super() calls the parent class constructor (Object class by default) — not mandatory,
     * but often added for clarity and consistency.
     */
    public Order(int orderId, int userId, int restaurantId, Integer addressId, double totalAmount, String status,
                 String paymentMethod, String paymentStatus, String deliveryInstructions, Date createdAt, Date updatedAt) {
        super(); // Calls Object() constructor — does nothing but keeps code explicit
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.addressId = addressId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.deliveryInstructions = deliveryInstructions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // -------- Getters and Setters --------
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // For debugging or logging order info
    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", userId=" + userId + ", restaurantId=" + restaurantId +
                ", addressId=" + addressId + ", totalAmount=" + totalAmount + ", status=" + status +
                ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus +
                ", deliveryInstructions=" + deliveryInstructions + ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt + "]";
    }
}
