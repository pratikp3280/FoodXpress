package com.app.controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.app.dao.UserDAO;
import com.app.dao_implementation.UserDAOImpl;
import com.app.models.User;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/user")  // Maps this servlet to /user URL
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDao;

    @Override
    public void init() throws ServletException {
       
    	// Initialize DAO object. Called once when servlet loads.
    	userDao = new UserDAOImpl();
    }

    // ----------------------------------------------------
    // GET Requests
    // ----------------------------------------------------
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "login";

        switch (action) {
            case "register":
                forward(request, response, "/jsp/register.jsp");
                break;

            case "login":
                forward(request, response, "/jsp/login.jsp");
                break;

            case "logout":
                doLogout(request, response);
                break;

            case "profile":
                showProfile(request, response);
                break;

            default:
                response.sendRedirect(request.getContextPath() + "/user?action=login");
        }
    }

    // ----------------------------------------------------
    // POST Requests
    // ----------------------------------------------------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        switch (action) {
            case "register":
                handleRegister(request, response);
                break;

            case "login":
                handleLogin(request, response);
                break;

            case "profileUpdate":
                handleProfileUpdate(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    // ----------------------------------------------------
    // Registration
    // ----------------------------------------------------
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String plainPassword = req.getParameter("password");

        if (username == null || username.isEmpty() || plainPassword == null || plainPassword.isEmpty()) {
            req.setAttribute("error", "Username and password are required.");
            forward(req, resp, "/jsp/register.jsp");
            return;
        }

        // Check if username exists
        if (userDao.getUserByUsername(username) != null) {
            req.setAttribute("error", "Username already exists.");
            forward(req, resp, "/jsp/register.jsp");
            return;
        }

        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));

        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setRole("customer");

        userDao.addUser(user);

        req.setAttribute("message", "Registration successful. Please login.");
        forward(req, resp, "/jsp/login.jsp");
    }

    // ----------------------------------------------------
    // Login
    // ----------------------------------------------------
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String passwordAttempt = req.getParameter("password");

        if (username == null || passwordAttempt == null) {
            req.setAttribute("error", "Username and password required.");
            forward(req, resp, "/jsp/login.jsp");
            return;
        }

        User user = userDao.getUserByUsername(username);

        if (user == null || !BCrypt.checkpw(passwordAttempt, user.getPassword())) {
            req.setAttribute("error", "Invalid username or password.");
            forward(req, resp, "/jsp/login.jsp");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute("loggedUser", user);
        session.setMaxInactiveInterval(30 * 60);

        resp.sendRedirect(req.getContextPath() + "/");
    }

    // ----------------------------------------------------
    // Logout
    // ----------------------------------------------------
    private void doLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        resp.sendRedirect(req.getContextPath() + "/user?action=login");
    }

    // ----------------------------------------------------
    // Profile
    // ----------------------------------------------------
    private void showProfile(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        forward(req, resp, "/jsp/profile.jsp");
    }

    // ----------------------------------------------------
    // Profile Update
    // ----------------------------------------------------
    private void handleProfileUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/user?action=login");
            return;
        }

        User logged = (User) session.getAttribute("loggedUser");

        logged.setName(req.getParameter("name"));
        logged.setEmail(req.getParameter("email"));
        logged.setPhone(req.getParameter("phone"));
        logged.setAddress(req.getParameter("address"));

        String newPassword = req.getParameter("newPassword");
        if (newPassword != null && !newPassword.isEmpty()) {
            logged.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
        }

        userDao.updateUser(logged);

        session.setAttribute("loggedUser", logged);
        req.setAttribute("message", "Profile updated successfully.");

        forward(req, resp, "/jsp/profile.jsp");
    }

    // ----------------------------------------------------
    // Utility
    // ----------------------------------------------------
    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(path);
        rd.forward(req, resp);
    }
}
