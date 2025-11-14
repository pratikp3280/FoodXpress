package com.app.models;

import java.util.Date;

/**
 * Represents a single menu item offered by a restaurant.
 * Mapped to the 'menu_items' table in the database.
 * Each item belongs to one restaurant (foreign key: restaurant_id).
 */
public class MenuItem {

    private int menuItemId;          // Primary key
    private int restaurantId;        // FK → restaurants.restaurant_id
    private String name;             // Name of the dish
    private String description;      // Short description
    private double price;            // Price of the item
    private String category;         // E.g., "Starters", "Main Course", "Desserts"
    private boolean isVeg;           // True = Veg, False = Non-Veg
    private boolean isAvailable;     // True = Available, False = Out of stock
    private String imageUrl;         // Optional image link
    private Date createdAt;          // Timestamp when the item was added

    // Default constructor
    public MenuItem() {
    }

    // Parameterized constructor
    public MenuItem(int menuItemId, int restaurantId, String name, String description,
                    double price, String category, boolean isVeg, boolean isAvailable,
                    String imageUrl, Date createdAt) {

        this.menuItemId = menuItemId;
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.isVeg = isVeg;
        this.isAvailable = isAvailable;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        // To ensure data integrity, we don’t allow negative prices
        if (price >= 0) {
            this.price = price;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean isVeg) {
        this.isVeg = isVeg;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MenuItem [menuItemId=" + menuItemId + ", restaurantId=" + restaurantId + ", name=" + name
                + ", description=" + description + ", price=" + price + ", category=" + category + ", isVeg=" + isVeg
                + ", isAvailable=" + isAvailable + ", imageUrl=" + imageUrl + ", createdAt=" + createdAt + "]";
    }
}
