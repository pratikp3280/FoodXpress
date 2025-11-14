package com.app.models;

import java.util.Date;

/**
 * Represents a Restaurant entity mapped to the 'restaurants' table in the database.
 * Each restaurant has basic info like name, cuisine type, contact details, and rating.
 */
public class Restaurant {

    private int restaurantId;
    private String name;
    private String description;
    private String cuisineType;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
    private String email;
    private double rating; // Decimal(3,2)
    private int deliveryTime; // Estimated delivery time in minutes
    private String imageUrl;
    private boolean isActive;
    private Date createdAt;

    // Default constructor
    public Restaurant() {
    }

    // Parameterized constructor
    public Restaurant(int restaurantId, String name, String description, String cuisineType,
                      String address, String city, String state, String zip,
                      String phone, String email, double rating, int deliveryTime,
                      String imageUrl, boolean isActive, Date createdAt) {

        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.cuisineType = cuisineType;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.rating = rating;
        this.deliveryTime = deliveryTime;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        // Ensure the rating stays between 0 and 5
        if (rating >= 0 && rating <= 5) {
            this.rating = rating;
        }
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Restaurant [restaurantId=" + restaurantId + ", name=" + name + ", description=" + description
                + ", cuisineType=" + cuisineType + ", address=" + address + ", city=" + city + ", state=" + state
                + ", zip=" + zip + ", phone=" + phone + ", email=" + email + ", rating=" + rating
                + ", deliveryTime=" + deliveryTime + ", imageUrl=" + imageUrl + ", isActive=" + isActive
                + ", createdAt=" + createdAt + "]";
    }
}




/*
 * When retrieving is_active from JDBC, remember to use rs.getBoolean("is_active").
If you ever face timezone mismatches with created_at, set your MySQL JDBC URL with:
?useTimezone=true&serverTimezone=UTC
 * */
