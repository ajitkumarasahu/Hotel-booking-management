package com.osrtc.bustracking.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to manage database connections.
 * Provides a single method to get a MySQL database connection.
 */
public class DBConnection {

    // Logger instance
    private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

    // JDBC URL of the MySQL database (host, port, database name)
    private static final String URL = "jdbc:mysql://localhost:3306/osrtc_db";

    // Database username
    private static final String USER = "root";

    // Database password (empty in this case)
    private static final String PASSWORD = "";

    /**
     * Establishes and returns a database connection.
     * @return Connection object to interact with the database
     */
    public static Connection getConnection() {
        Connection conn = null; // Initialize connection object

        try {
            // Load the MySQL JDBC driver dynamically
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create a connection to the database using DriverManager
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Log successful connection
            logger.info("Database connected successfully!");
        } catch (Exception e) {
            // Log connection errors
            logger.log(Level.SEVERE, "Database connection failed: " + e.getMessage(), e);
        }

        // Return the Connection object (null if connection failed)
        return conn;
    }
}
