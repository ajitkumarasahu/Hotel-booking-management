package com.osrtc.bustracking.model;

import java.util.logging.Logger;

/**
 * Represents an Admin entity in the OSRTC Bus Tracking System.
 * Stores basic authentication and identification details.
 */
public class Admin {

    private static final Logger logger = Logger.getLogger(Admin.class.getName());

    /** Unique identifier for the admin */
    private int adminId;

    /** Username for login */
    private String username;

    /** Password for login (should be stored securely, e.g., hashed) */
    private String password;

    /** Gets the admin's unique ID. */
    public int getAdminId() {
        logger.info("Getting adminId: " + adminId);
        return adminId;
    }

    /** Sets the admin's unique ID. */
    public void setAdminId(int adminId) {
        logger.info("Setting adminId: " + adminId);
        this.adminId = adminId;
    }

    /** Gets the admin's username. */
    public String getUsername() {
        logger.info("Getting username: " + username);
        return username;
    }

    /** Sets the admin's username. */
    public void setUsername(String username) {
        logger.info("Setting username: " + username);
        this.username = username;
    }

    /** Gets the admin's password. */
    public String getPassword() {
        logger.info("Getting password for adminId: " + adminId);
        return password;
    }

    /** Sets the admin's password. */
    public void setPassword(String password) {
        logger.info("Setting password for adminId: " + adminId);
        this.password = password;
    }
}
