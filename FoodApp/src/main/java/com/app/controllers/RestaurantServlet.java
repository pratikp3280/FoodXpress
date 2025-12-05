package com.app.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.dao.MenuItemDAO;
import com.app.dao.RestaurantDAO;
import com.app.dao_implementation.MenuItemDAOImpl;
import com.app.dao_implementation.RestaurantDAOImpl;
import com.app.models.MenuItem;
import com.app.models.Restaurant;

/*
 * This RestaurantServlet controls:
 *
 * ✔ Listing all restaurants
 * ✔ Viewing a single restaurant's details
 * ✔ Redirecting/Forwarding to restaurant JSP pages
 *
 * Pattern used:
 * - doGet → page display (list, view)
 * - doPost → not used (no form submission yet)
 */

@WebServlet("/restaurant")
public class RestaurantServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DAO object for database operations
    private RestaurantDAO restaurantDAO;

    @Override
    public void init() throws ServletException {
        // Initialize DAO once when servlet starts
        restaurantDAO = new RestaurantDAOImpl();
    }

    // ----------------------------------------------------
    // GET Requests (display pages)
    // ----------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Determine which action user wants (default = list restaurants)
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {

            case "view":    // Show a single restaurant details
                showRestaurantDetails(request, response);
                break;

            case "list":    // Show all restaurants
            default:
                listRestaurants(request, response);
                break;
        }
    }

    // ----------------------------------------------------
    // POST Requests (currently unused for restaurants)
    // ----------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // No POST-use cases yet → return error
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    // ----------------------------------------------------
    // List All Restaurants
    // URL → /restaurant?action=list
    // ----------------------------------------------------
    private void listRestaurants(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get list from database
        List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();

        // Send list to JSP
        req.setAttribute("restaurants", restaurants);

        // Forward to the JSP page
        forward(req, resp, "/jsp/customer/restaurantList.jsp");

    }

    // ----------------------------------------------------
    // View Restaurant Details
    // URL → /restaurant?action=view&id=10
    // ----------------------------------------------------
    private void showRestaurantDetails(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        if (idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/restaurant?action=list");
            return;
        }

        int restaurantId = Integer.parseInt(idParam);

        // 1️⃣ Restaurant
        Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);
        if (restaurant == null || !restaurant.isActive()) {
            resp.sendRedirect(req.getContextPath() + "/restaurant?action=list");
            return;
        }

        // 2️⃣ Menu
        MenuItemDAO menuItemDAO = new MenuItemDAOImpl();
        List<MenuItem> menuItems = menuItemDAO.getMenuItemsByRestaurantId(restaurantId);

        // 3️⃣ Attach
        req.setAttribute("restaurant", restaurant);
        req.setAttribute("menuItems", menuItems);

        forward(req, resp, "/jsp/customer/restaurantDetails.jsp");
    }

    // ----------------------------------------------------
    // Utility Function → forward to JSP
    // ----------------------------------------------------
    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(path);
        rd.forward(req, resp);
    }
}


/*Action	       				URL	                                   Purpose
list (default):	           /restaurant?action=list	        To Show all restaurants
view	      :            /restaurant?action=view&id=3	To Show restaurant details
 * 
 * **/
