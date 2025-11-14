package com.app.dao;

import java.util.List;

import com.app.models.Restaurant;

/**This DAO will handle all restaurant-related operations such as:
*	Adding new restaurants (admin)
*	Fetching restaurant by ID
*	Listing all restaurants
*	Filtering by cuisine, city, or rating
*	Updating and deleting restaurants
 * 
 */
public interface RestaurantDAO {

	 // ➕ Add a new restaurant (Admin functionality)
    void addRestaurant(Restaurant restaurant);
    
    // 🔍 Get restaurant details by its ID
    Restaurant getRestaurantById(int restaurantId);
    
    // 📜 Retrieve all active restaurants
    List<Restaurant> getAllRestaurants();
    
    // 🔎 Filter restaurants by cuisine type (e.g., Indian, Chinese)
    List<Restaurant> getRestaurantsByCuisine(String cuisineType);
    
    // 📍 Filter restaurants by city/location
    List<Restaurant> getRestaurantsByCity(String city);
    
    // ⭐ Filter restaurants based on minimum rating (e.g., 4.0+)
    List<Restaurant> getRestaurantsByRating(double minRating);
    
    // ✏️ Update restaurant details (e.g., name, description, rating)
    void updateRestaurant(Restaurant restaurant);
    
    // ❌ Deactivate or delete a restaurant
    void deleteRestaurant(int restaurantId);
    
   // Users can simply type “Delhi Indian” or “pizza” and get relevant results dynamically
    // search bar or text-based search functionality.
    List<Restaurant> searchRestaurants(String searchTerm);
}
