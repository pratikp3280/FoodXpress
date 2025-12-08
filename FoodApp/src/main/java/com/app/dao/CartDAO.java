package com.app.dao;

import com.app.models.Cart;
import com.app.models.CartItem;
import java.util.List;

/**
 * 🛒 CartDAO Interface
 * --------------------
 * Defines all operations related to managing a user's cart —
 * including adding, updating, removing items, and fetching total cart details.
 */
public interface CartDAO {

    // 🛒 Get the active cart of a user by their ID
    Cart getCartByUserId(int userId);

    // 🆕 Create a new cart for a user (only if they don’t already have one)
    void createCart(int userId);

    // ➕ Add a menu item to the user's cart with the specified quantity
    void addItemToCart(int userId, int menuItemId, int quantity);

    // 🔁 Update the quantity of a specific item already present in the cart
    void updateCartItemQuantity(int cartId, int menuItemId, int quantity);

    // ❌ Remove a specific item from the user's cart
    void removeItemFromCart(int cartId, int menuItemId);

    // 📋 Get a list of all cart items for a specific cart
    List<CartItem> getCartItems(int cartId);

    // 🧹 Remove all items from the cart (useful after checkout or reset)
    void clearCart(int cartId);

    // 💰 Calculate and return the total cost of all items in the cart
    double getCartTotal(int cartId);

    // 🔎 Check if a specific menu item is already present in the cart
    boolean isItemInCart(int cartId, int menuItemId);
}