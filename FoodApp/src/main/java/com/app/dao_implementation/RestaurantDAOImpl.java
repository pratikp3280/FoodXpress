package com.app.dao_implementation;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.dao.RestaurantDAO;
import com.app.models.Restaurant;
import com.app.utility.DBConnection;


/**
 * ✅ Implementation class for RestaurantDAO interface.
 * Handles all database operations related to Restaurants.
 * PreparedStatement → prevents SQL injection.
 */
public class RestaurantDAOImpl implements RestaurantDAO {

    // ==========================================================
    // 🔹 SQL Query Constants
    // ==========================================================
    private static final String INSERT_RESTAURANT_QUERY =
        "INSERT INTO restaurants (name, description, cuisine_type, address, city, state, zip, phone, email, rating, delivery_time, image_url, is_active) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_RESTAURANT_BY_ID =
        "SELECT * FROM restaurants WHERE restaurant_id = ?";

    private static final String SELECT_ALL_ACTIVE_RESTAURANTS =
        "SELECT * FROM restaurants WHERE is_active = TRUE";

    private static final String SELECT_BY_CUISINE =
        "SELECT * FROM restaurants WHERE cuisine_type = ? AND is_active = TRUE";

    private static final String SELECT_BY_CITY =
        "SELECT * FROM restaurants WHERE city = ? AND is_active = TRUE";

    private static final String SELECT_BY_MIN_RATING =
        "SELECT * FROM restaurants WHERE rating >= ? AND is_active = TRUE";

    private static final String UPDATE_RESTAURANT_QUERY =
        "UPDATE restaurants SET name=?, description=?, cuisine_type=?, address=?, city=?, state=?, zip=?, phone=?, email=?, rating=?, delivery_time=?, image_url=?, is_active=? "
        + "WHERE restaurant_id=?";

    private static final String DEACTIVATE_RESTAURANT_QUERY =
        "UPDATE restaurants SET is_active = FALSE WHERE restaurant_id = ?";

    private static final String SEARCH_RESTAURANTS_QUERY =
        "SELECT * FROM restaurants "
        + "WHERE (name LIKE ? OR cuisine_type LIKE ? OR city LIKE ?) AND is_active = TRUE";


    // ==========================================================
    // 🔹 1️⃣ Add a new Restaurant (Admin functionality)
    // ==========================================================
    @Override
    public void addRestaurant(Restaurant restaurant) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_RESTAURANT_QUERY)) {

            // Map Restaurant object fields → PreparedStatement
            ps.setString(1, restaurant.getName());
            ps.setString(2, restaurant.getDescription());
            ps.setString(3, restaurant.getCuisineType());
            ps.setString(4, restaurant.getAddress());
            ps.setString(5, restaurant.getCity());
            ps.setString(6, restaurant.getState());
            ps.setString(7, restaurant.getZip());
            ps.setString(8, restaurant.getPhone());
            ps.setString(9, restaurant.getEmail());
            ps.setDouble(10, restaurant.getRating());
            ps.setInt(11, restaurant.getDeliveryTime());
            ps.setString(12, restaurant.getImageUrl());
            ps.setBoolean(13, restaurant.isActive());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Restaurant added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error adding restaurant: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ==========================================================
    // 🔹 2️⃣ Get restaurant by ID
    // ==========================================================
    @Override
    public Restaurant getRestaurantById(int restaurantId) {
        Restaurant restaurant = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_RESTAURANT_BY_ID)) {

            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    restaurant = extractRestaurantFromResultSet(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching restaurant by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurant;
    }


    // ==========================================================
    // 🔹 3️⃣ Get all active restaurants
    // ==========================================================
    @Override
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ACTIVE_RESTAURANTS)) {

            while (rs.next()) {
                restaurants.add(extractRestaurantFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching all restaurants: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurants;
    }


    // ==========================================================
    // 🔹 4️⃣ Get restaurants by cuisine type
    // ==========================================================
    @Override
    public List<Restaurant> getRestaurantsByCuisine(String cuisineType) {
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_CUISINE)) {

            ps.setString(1, cuisineType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(extractRestaurantFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching restaurants by cuisine: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurants;
    }


    // ==========================================================
    // 🔹 5️⃣ Get restaurants by city
    // ==========================================================
    @Override
    public List<Restaurant> getRestaurantsByCity(String city) {
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_CITY)) {

            ps.setString(1, city);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(extractRestaurantFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching restaurants by city: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurants;
    }


    // ==========================================================
    // 🔹 6️⃣ Get restaurants by minimum rating
    // ==========================================================
    @Override
    public List<Restaurant> getRestaurantsByRating(double minRating) {
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_MIN_RATING)) {

            ps.setDouble(1, minRating);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(extractRestaurantFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching restaurants by rating: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurants;
    }


    // ==========================================================
    // 🔹 7️⃣ Update restaurant details
    // ==========================================================
    @Override
    public void updateRestaurant(Restaurant restaurant) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_RESTAURANT_QUERY)) {

        	pstmt.setString(1, restaurant.getName());
        	pstmt.setString(2, restaurant.getDescription());
        	pstmt.setString(3, restaurant.getCuisineType());
        	pstmt.setString(4, restaurant.getAddress());
        	pstmt.setString(5, restaurant.getCity());
        	pstmt.setString(6, restaurant.getState());
        	pstmt.setString(7, restaurant.getZip());
        	pstmt.setString(8, restaurant.getPhone());
        	pstmt.setString(9, restaurant.getEmail());
        	pstmt.setDouble(10, restaurant.getRating());
        	pstmt.setInt(11, restaurant.getDeliveryTime());
        	pstmt.setString(12, restaurant.getImageUrl());
        	pstmt.setBoolean(13, restaurant.isActive());
        	pstmt.setInt(14, restaurant.getRestaurantId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Restaurant updated successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error updating restaurant: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ==========================================================
    // 🔹 8️⃣ Deactivate (soft-delete) a restaurant
    // ==========================================================
    @Override
    public void deleteRestaurant(int restaurantId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DEACTIVATE_RESTAURANT_QUERY)) {

            ps.setInt(1, restaurantId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("❌ Restaurant deactivated successfully.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error deactivating restaurant: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // ==========================================================
    // 🔹 9️⃣ Search restaurants dynamically
    // ==========================================================
    @Override
    public List<Restaurant> searchRestaurants(String searchTerm) {
        List<Restaurant> restaurants = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_RESTAURANTS_QUERY)) {
/* SQL query for better understanding:
 * 
 *   SELECT * FROM restaurants
	 WHERE (name LIKE ? OR cuisine_type LIKE ? OR city LIKE ?)
	 AND is_active = TRUE
 */

            String pattern = "%" + searchTerm + "%";
            ps.setString(1, pattern);                          // name
            ps.setString(2, pattern);                          // cuisine_type
            ps.setString(3, pattern);                          // city

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    restaurants.add(extractRestaurantFromResultSet(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error searching restaurants: " + e.getMessage());
            e.printStackTrace();
        }

        return restaurants;
    }


    // ==========================================================
    // ♻️ Helper Method — Maps ResultSet → Restaurant Object
    // ==========================================================
    private Restaurant extractRestaurantFromResultSet(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();

        restaurant.setRestaurantId(rs.getInt("restaurant_id"));
        restaurant.setName(rs.getString("name"));
        restaurant.setDescription(rs.getString("description"));
        restaurant.setCuisineType(rs.getString("cuisine_type"));
        restaurant.setAddress(rs.getString("address"));
        restaurant.setCity(rs.getString("city"));
        restaurant.setState(rs.getString("state"));
        restaurant.setZip(rs.getString("zip"));
        restaurant.setPhone(rs.getString("phone"));
        restaurant.setEmail(rs.getString("email"));
        restaurant.setRating(rs.getDouble("rating"));
        restaurant.setDeliveryTime(rs.getInt("delivery_time"));
        restaurant.setImageUrl(rs.getString("image_url"));
        restaurant.setActive(rs.getBoolean("is_active"));
        restaurant.setCreatedAt(rs.getTimestamp("created_at"));

        return restaurant;
    }
}
