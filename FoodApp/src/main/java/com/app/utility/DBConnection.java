package com.app.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to manage MySQL database connections using an external properties file.
 */
public class DBConnection {

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        try {
            // Load properties file from classpath
            InputStream input = DBConnection.class
                    .getClassLoader()
                    .getResourceAsStream("app.properties");

            Properties prop = new Properties();
            prop.load(input);

            URL = prop.getProperty("db.url");
            USERNAME = prop.getProperty("db.username");
            PASSWORD = prop.getProperty("db.password");

            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (Exception e) {
            System.err.println("❌ Failed to load database configuration!");
            e.printStackTrace();
        }
    }

    /**
     * Returns a database connection using the loaded properties.
     */
    public static Connection getConnection() {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            e.printStackTrace();
        }

        return connection; // return the connection (can be null if failed)
    }
}
