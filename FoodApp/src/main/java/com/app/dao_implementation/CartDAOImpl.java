package com.app.dao_implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.app.dao.CartDAO;
import com.app.models.Cart;
import com.app.models.CartItem;
import com.app.utility.DBConnection;

/**
 * ✅ Implementation of CartDAO Interface.
 * Handles all CRUD and business logic for user's cart and cart_items table.
 */
public class CartDAOImpl implements CartDAO {

    // -------------------------------------------------------------------------
    // 🧩 SQL QUERIES
    // -------------------------------------------------------------------------
    private static final String GET_CART_BY_USER_ID =
            "SELECT * FROM carts WHERE user_id = ?";

    private static final String CREATE_CART =
            "INSERT INTO carts (user_id) VALUES (?)";

    private static final String ADD_ITEM_TO_CART =
            "INSERT INTO cart_items (cart_id, menu_item_id, quantity) VALUES (?, ?, ?) "
          + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

    private static final String UPDATE_ITEM_QUANTITY =
            "UPDATE cart_items SET quantity = ? WHERE cart_id = ? AND menu_item_id = ?";

    private static final String REMOVE_ITEM_FROM_CART =
            "DELETE FROM cart_items WHERE cart_id = ? AND menu_item_id = ?";

    private static final String GET_CART_ITEMS =
            "SELECT * FROM cart_items WHERE cart_id = ?";

    private static final String CLEAR_CART =
            "DELETE FROM cart_items WHERE cart_id = ?";

    private static final String GET_CART_TOTAL =
            "SELECT SUM(ci.quantity * mi.price) AS total "
          + "FROM cart_items ci JOIN menu_items mi ON ci.menu_item_id = mi.menu_item_id "
          + "WHERE ci.cart_id = ?";

    private static final String CHECK_ITEM_IN_CART =
            "SELECT COUNT(*) FROM cart_items WHERE cart_id = ? AND menu_item_id = ?";


    // -------------------------------------------------------------------------
    // 🛒 Get the active cart of a user by their ID
    // -------------------------------------------------------------------------
    @Override
    public Cart getCartByUserId(int userId) {
        Cart cart = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_CART_BY_USER_ID)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cart = extractCart(rs);
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching cart for user: " + e.getMessage());
            e.printStackTrace();
        }
        return cart;
    }

    // -------------------------------------------------------------------------
    // 🆕 Create a new cart for a user (if not exists)
    // -------------------------------------------------------------------------
    @Override
    public void createCart(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_CART)) {

            ps.setInt(1, userId);
            ps.executeUpdate();
            System.out.println("✅ New cart created for user ID: " + userId);

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("⚠️ Cart already exists for user ID: " + userId);
            } else {
                System.err.println("❌ Error creating cart: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // -------------------------------------------------------------------------
    // ➕ Add an item to user's cart
    // -------------------------------------------------------------------------
    @Override
    public void addItemToCart(int userId, int menuItemId, int quantity) {
        // Get or create a cart for this user
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            createCart(userId);
            cart = getCartByUserId(userId);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(ADD_ITEM_TO_CART)) {

            ps.setInt(1, cart.getCartId());
            ps.setInt(2, menuItemId);
            ps.setInt(3, quantity);

            ps.executeUpdate();
            System.out.println("✅ Item added/updated in cart successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error adding item to cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // 🔁 Update the quantity of a specific cart item
    // -------------------------------------------------------------------------
    @Override
    public void updateCartItemQuantity(int cartId, int menuItemId, int quantity) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ITEM_QUANTITY)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, menuItemId);

            ps.executeUpdate();
            System.out.println("✅ Cart item quantity updated!");

        } catch (SQLException e) {
            System.err.println("❌ Error updating cart item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // ❌ Remove a specific item from the cart
    // -------------------------------------------------------------------------
    @Override
    public void removeItemFromCart(int cartId, int menuItemId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(REMOVE_ITEM_FROM_CART)) {

            ps.setInt(1, cartId);
            ps.setInt(2, menuItemId);
            ps.executeUpdate();

            System.out.println("🗑️ Item removed from cart successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error removing item from cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // 📋 Get all cart items for a specific cart
    // -------------------------------------------------------------------------
    @Override
    public List<CartItem> getCartItems(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_CART_ITEMS)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cartItems.add(extractCartItem(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Error fetching cart items: " + e.getMessage());
            e.printStackTrace();
        }
        return cartItems;
    }

    // -------------------------------------------------------------------------
    // 🧹 Clear all items from a cart
    // -------------------------------------------------------------------------
    @Override
    public void clearCart(int cartId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CLEAR_CART)) {

            ps.setInt(1, cartId);
            ps.executeUpdate();

            System.out.println("🧹 Cart cleared successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Error clearing cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // 💰 Calculate total price of items in cart
    // -------------------------------------------------------------------------
    @Override
    public double getCartTotal(int cartId) {
        double total = 0.0;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_CART_TOTAL)) {

            ps.setInt(1, cartId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error calculating cart total: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }

    // -------------------------------------------------------------------------
    // 🔎 Check if an item already exists in the cart
    // -------------------------------------------------------------------------
    @Override
    public boolean isItemInCart(int cartId, int menuItemId) {
        boolean exists = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_ITEM_IN_CART)) {

            ps.setInt(1, cartId);
            ps.setInt(2, menuItemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error checking item in cart: " + e.getMessage());
            e.printStackTrace();
        }
        return exists;
    }

    // -------------------------------------------------------------------------
    // ♻️ Helper method: Convert ResultSet → Cart
    // -------------------------------------------------------------------------
    private Cart extractCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setCreatedAt(rs.getTimestamp("created_at"));
        cart.setUpdatedAt(rs.getTimestamp("updated_at"));
        return cart;
    }

    // -------------------------------------------------------------------------
    // ♻️ Helper method: Convert ResultSet → CartItem
    // -------------------------------------------------------------------------
    private CartItem extractCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setCartId(rs.getInt("cart_id"));
        item.setMenuItemId(rs.getInt("menu_item_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setAddedAt(rs.getTimestamp("added_at"));
        return item;
    }
}
