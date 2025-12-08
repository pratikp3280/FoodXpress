package com.app.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.CartDAO;
import com.app.dao.MenuItemDAO;
import com.app.dao_implementation.CartDAOImpl;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.models.Cart;
import com.app.models.CartItem;
import com.app.models.MenuItem;
import com.app.models.User; 

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO = new CartDAOImpl();
    private MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) action = "view";

        switch (action) {
            case "view":
                showCart(req, resp);
                break;

            case "remove":
                removeItem(req, resp);
                break;

            case "clear":
                clearCart(req, resp);
                break;

            default:
                showCart(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if (action == null) action = "view";

        switch (action) {
            case "add":
                addItem(req, resp);
                break;

            case "update":
                updateItem(req, resp);
                break;

            default:
                resp.sendRedirect("cart?action=view");
        }
    }

    // =========================================================
    // VIEW CART / show Cart
    // =========================================================
    private void showCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        int userId = loggedUser.getUserId();

        // Ensure cart exists
        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) {
            cartDAO.createCart(userId);
            cart = cartDAO.getCartByUserId(userId);
        }

        List<CartItem> cartItems = cartDAO.getCartItems(cart.getCartId());

        // Fetch MenuItem details for display (name, price, image)
        java.util.Map<Integer, MenuItem> menuMap = new java.util.HashMap<>();

        for (CartItem ci : cartItems) {
            MenuItem item = menuItemDAO.getMenuItemById(ci.getMenuItemId());
            if (item != null) menuMap.put(ci.getMenuItemId(), item);
        }

        double total = cartDAO.getCartTotal(cart.getCartId());

        req.setAttribute("cartItems", cartItems);
        req.setAttribute("menuItems", menuMap);
        req.setAttribute("cartTotal", total);

        RequestDispatcher rd =
        	    req.getRequestDispatcher("/jsp/customer/cart.jsp");
        	rd.forward(req, resp);

    }

    // =========================================================
    // ADD ITEM TO CART
    // =========================================================
    private void addItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            // If AJAX, return 401 JSON. If normal, redirect.
            if (isAjax(req)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\":false, \"error\":\"not_authenticated\"}");
                return;
            } else {
                resp.sendRedirect("login.jsp");
                return;
            }
        }
        int userId = loggedUser.getUserId();

        int menuItemId;
        int quantity;
        try {
            menuItemId = Integer.parseInt(req.getParameter("menuItemId"));
            quantity = Integer.parseInt(req.getParameter("quantity"));
        } catch (NumberFormatException e) {
            if (isAjax(req)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"success\":false, \"error\":\"invalid_parameters\"}");
                return;
            } else {
                resp.sendRedirect("cart?action=view");
                return;
            }
        }

        // Ensure cart exists
        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) cartDAO.createCart(userId);

        // Perform DB insert/update
        cartDAO.addItemToCart(userId, menuItemId, quantity);

        // If AJAX: return JSON success and total item count
        if (isAjax(req)) {
            cart = cartDAO.getCartByUserId(userId);
            int cartCount = 0;
            if (cart != null) {
                for (CartItem ci : cartDAO.getCartItems(cart.getCartId())) {
                    cartCount += ci.getQuantity();
                }
            }
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"success\":true,\"cartCount\":" + cartCount + "}");
            return;
        }

        // Non-AJAX: redirect to view cart
        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // UPDATE ITEM QUANTITY
    // =========================================================
    private void updateItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        int userId = loggedUser.getUserId();

        int menuItemId = Integer.parseInt(req.getParameter("menuItemId"));
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart != null) {
            cartDAO.updateCartItemQuantity(cart.getCartId(), menuItemId, quantity);
        }

        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // REMOVE ITEM FROM CART
    // =========================================================
    private void removeItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        int userId = loggedUser.getUserId();

        int menuItemId = Integer.parseInt(req.getParameter("menuItemId"));

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart != null) {
            cartDAO.removeItemFromCart(cart.getCartId(), menuItemId);
        }

        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // CLEAR CART
    // =========================================================
    private void clearCart(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        User loggedUser = session != null ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }
        int userId = loggedUser.getUserId();

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart != null) {
            cartDAO.clearCart(cart.getCartId());
        }

        resp.sendRedirect("cart?action=view");
    }

    // =========================================================
    // Utility: detect AJAX requests
    // =========================================================
    private boolean isAjax(HttpServletRequest req) {
        String xhr = req.getHeader("X-Requested-With");
        return xhr != null && "XMLHttpRequest".equalsIgnoreCase(xhr);
    }
}
