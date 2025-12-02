package com.app.controllers;

import com.app.dao.AddressDAO;
import com.app.dao_implementation.AddressDAOImpl;
import com.app.models.Address;
import com.app.models.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/*
 * AddressServlet
 * ----------------------------------------------------
 * This servlet handles:
 * 
 * ✔ Viewing all saved addresses of logged-in customer
 * ✔ Adding a new address
 * ✔ Editing an existing address
 * ✔ Deleting an address
 * 
 * SECURITY:
 * ✔ Requires user login
 * ✔ User can access ONLY their own addresses
 * 
 * URL Mapping:
 * /customer/addresses
 */

@WebServlet("/customer/addresses")
public class AddressServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // DAO for database access
    private AddressDAO addressDAO;

    @Override
    public void init() throws ServletException {
        // Initialize DAO once when servlet loads
        addressDAO = new AddressDAOImpl();
    }

    // ----------------------------------------------------
    // GET REQUESTS → Show addresses / load edit form
    // ----------------------------------------------------
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1️⃣ AUTHENTICATION CHECK
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            // User not logged in → redirect to login
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        int userId = user.getUserId(); // ✅ Correct and secure

        // 2️⃣ READ REQUEST PARAMETERS
        String action = req.getParameter("action");
        String addressIdParam = req.getParameter("id");

        // 3️⃣ IF EDIT REQUEST → LOAD ADDRESS
        if ("edit".equalsIgnoreCase(action) && addressIdParam != null) {

            int addressId = Integer.parseInt(addressIdParam);

            // ✅ SECURITY: Fetch address ONLY if it belongs to this user
            Address editAddress = addressDAO.getAddressById(addressId, userId);

            if (editAddress != null) {
                req.setAttribute("editAddress", editAddress);
            }
        }

        // 4️⃣ FETCH ALL ADDRESSES OF LOGGED-IN USER
        List<Address> addresses = addressDAO.getAddressesByUserId(userId);
        req.setAttribute("addresses", addresses);

        // 5️⃣ FORWARD TO JSP
        req.getRequestDispatcher("/jsp/customer/addresses.jsp")
           .forward(req, resp);
    }

    // ----------------------------------------------------
    // POST REQUESTS → Add / Update / Delete Address
    // ----------------------------------------------------
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // 1️⃣ AUTHENTICATION CHECK
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        int userId = user.getUserId();

        // 2️⃣ READ ACTION
        String action = req.getParameter("action");

        // ------------------------------------------------
        // DELETE ADDRESS
        // ------------------------------------------------
        if ("delete".equalsIgnoreCase(action)) {

            int addressId = Integer.parseInt(req.getParameter("id"));

            // ✅ SECURITY: delete only user's address
            addressDAO.deleteAddress(addressId, userId);

            resp.sendRedirect(req.getContextPath() + "/customer/addresses");
            return;
        }

        // ------------------------------------------------
        // COMMON FIELDS (Add / Update)
        // ------------------------------------------------
        String street = req.getParameter("street");
        String city = req.getParameter("city");
        String state = req.getParameter("state");
        String zip = req.getParameter("zip");
        String landmark = req.getParameter("landmark");

        // ------------------------------------------------
        // UPDATE ADDRESS
        // ------------------------------------------------
        if ("update".equalsIgnoreCase(action)) {

            int addressId = Integer.parseInt(req.getParameter("id"));

            Address address = new Address();
            address.setAddressId(addressId);
            address.setUserId(userId);
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setZip(zip);
            address.setLandmark(landmark);

            addressDAO.updateAddress(address);

            resp.sendRedirect(req.getContextPath() + "/customer/addresses");
            return;
        }

        // ------------------------------------------------
        // ADD NEW ADDRESS
        // ------------------------------------------------
        if ("add".equalsIgnoreCase(action)) {

            Address address = new Address();
            address.setUserId(userId);
            address.setStreet(street);
            address.setCity(city);
            address.setState(state);
            address.setZip(zip);
            address.setLandmark(landmark);

            addressDAO.addAddress(address);

            resp.sendRedirect(req.getContextPath() + "/customer/addresses");
        }
    }
}
