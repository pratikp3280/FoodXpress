package com.app.controllers;

import java.io.IOException;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.CartDAO;
import com.app.dao.MenuItemDAO;
import com.app.dao.OrderDAO;
import com.app.dao_implementation.CartDAOImpl;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.dao_implementation.OrderDAOImpl;

import com.app.models.Cart;
import com.app.models.CartItem;
import com.app.models.MenuItem;
import com.app.models.Order;
import com.app.models.OrderItem;
import com.app.models.User;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAOImpl();
    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();
    private final OrderDAO orderDAO = new OrderDAOImpl();

    /* ==========================================================
       GET  : SHOW CHECKOUT PAGE
       ========================================================== */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // ✅ SINGLE SOURCE OF AUTH TRUTH
        User user = (session == null)
                ? null
                : (User) session.getAttribute("loggedUser");

        // ✅ NOT LOGGED IN
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        int userId = user.getUserId();

        Cart cart = cartDAO.getCartByUserId(userId);

        // ✅ CART NOT FOUND / EMPTY
        if (cart == null) {
            resp.sendRedirect(req.getContextPath() + "/cart?action=view");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItems(cart.getCartId());

        if (cartItems == null || cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart?action=view");
            return;
        }

        double totalAmount = cartDAO.getCartTotal(cart.getCartId());

        // ✅ BUILD MENU ITEM MAP
        Map<Integer, MenuItem> menuItems = new HashMap<>();
        for (CartItem ci : cartItems) {
            menuItems.put(ci.getMenuItemId(),
                    menuItemDAO.getMenuItemById(ci.getMenuItemId()));
        }

        // ✅ SET ATTRIBUTES
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("menuItems", menuItems);
        req.setAttribute("totalAmount", totalAmount);

        // ✅ FORWARD TO JSP (NO REDIRECT)
        req.getRequestDispatcher("/jsp/customer/checkout.jsp")
                .forward(req, resp);
    }

    /* ==========================================================
       POST : PLACE ORDER
       ========================================================== */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // ✅ SINGLE SOURCE OF AUTH TRUTH
        User user = (session == null)
                ? null
                : (User) session.getAttribute("loggedUser");

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        int userId = user.getUserId();

        Cart cart = cartDAO.getCartByUserId(userId);

        if (cart == null) {
            resp.sendRedirect(req.getContextPath() + "/cart?action=view");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartItems(cart.getCartId());

        if (cartItems == null || cartItems.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart?action=view");
            return;
        }

        /* ================= READ CHECKOUT FORM ================= */
        /* ================= READ CHECKOUT FORM ================= */
        int restaurantId = Integer.parseInt(req.getParameter("restaurantId"));

        String paymentMethod = req.getParameter("payment_method");
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            paymentMethod = "COD"; // fallback safety
        }

        String deliveryInstructions = req.getParameter("instructions");

        /* ✅ SAFE ADDRESS PARSING */
        String addressIdStr = req.getParameter("addressId");
        Integer addressId = null;
        if (addressIdStr != null && !addressIdStr.trim().isEmpty()) {
            addressId = Integer.parseInt(addressIdStr);
        }

        double totalAmount = cartDAO.getCartTotal(cart.getCartId());


        /* ================= CREATE ORDER ================= */
        Order order = new Order();
        order.setUserId(userId);
        order.setRestaurantId(restaurantId);
        order.setAddressId(addressId);
        order.setTotalAmount(totalAmount);
        order.setStatus("placed");
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("pending");
        order.setDeliveryInstructions(deliveryInstructions);

        /* ================= ORDER ITEMS ================= */
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cartItems) {
            MenuItem mi = menuItemDAO.getMenuItemById(ci.getMenuItemId());

            OrderItem oi = new OrderItem();
            oi.setMenuItemId(ci.getMenuItemId());
            oi.setQuantity(ci.getQuantity());
            oi.setPriceAtOrder(mi.getPrice());

            orderItems.add(oi);
        }

        int orderId = orderDAO.placeOrder(order, orderItems);

        if (orderId <= 0) {
            req.setAttribute("errorMessage",
                    "Order could not be placed due to a system error.");
            req.getRequestDispatcher("/jsp/customer/checkout.jsp")
                    .forward(req, resp);
            return;
        }

        /* ================= CLEANUP & REDIRECT ================= */
        cartDAO.clearCart(cart.getCartId());

        resp.sendRedirect(req.getContextPath()
                + "/orderSuccess?orderId=" + orderId);
    }
}
