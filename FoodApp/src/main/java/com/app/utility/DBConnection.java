package com.app.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class to manage MySQL database connections.
 * Provides a single reusable method to get a Connection object.
 */
public class DBConnection {

    // ---------- Database Configuration ----------
    private static final String URL = "jdbc:mysql://localhost:3306/food_delivery_app"; // DB URL
    private static final String USERNAME = "root";   // DB username
    private static final String PASSWORD = "Pratik@123.in"; // DB password

    /**
     * Establishes and returns a database connection.
     */
    public static Connection getConnection() {
        Connection connection = null; // holds the connection

        try {
            // Load MySQL JDBC driver (registers driver with DriverManager)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found. Please add MySQL Connector JAR to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed! Check URL, username, or password.");
            e.printStackTrace();
        }

        return connection; // return the connection (can be null if failed)
    }
}
