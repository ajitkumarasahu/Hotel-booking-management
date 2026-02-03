package com.osrtc.bustracking.service;
// Package declaration – this class is part of the service layer.

import com.osrtc.bustracking.dao.AdminDAO;
// Imports the DAO class that interacts with the database for Admin entities.

import com.osrtc.bustracking.model.Admin;
// Imports the Admin model class.

import java.util.List;
// Imports List for returning multiple Admins.

import java.util.logging.Logger;
// Imports Logger for logging messages.

/**
 * Service layer for Admin operations.
 * Handles business logic and validation before interacting with AdminDAO.
 */
public class AdminService {

    private final AdminDAO adminDAO = new AdminDAO();
    // Instantiates DAO for database operations.

    private static final Logger logger = Logger.getLogger(AdminService.class.getName());
    // Logger instance for this class.

    public List<Admin> getAllAdmins() {
        logger.info("Fetching all admins");
        return adminDAO.getAllAdmins();
        // Retrieves all admins from the database via DAO.
    }

    public Admin getAdminById(int id) {
        Admin admin = adminDAO.getAdminById(id);
        // Fetch admin by ID.

        if (admin == null) {
            logger.warning("Admin not found: id=" + id);
            throw new IllegalArgumentException("Admin not found.");
            // Throws exception if admin does not exist.
        }
        return admin;
    }

    public boolean addAdmin(Admin admin) {
        if (admin.getUsername() == null || admin.getPassword() == null) {
            throw new IllegalArgumentException("Username and Password are required.");
            // Validates that username and password are provided.
        }
        logger.info("Adding new admin: " + admin.getUsername());
        return adminDAO.addAdmin(admin);
        // Delegates to DAO to insert the admin into the database.
    }

    public boolean updateAdmin(Admin admin) {
        if (adminDAO.getAdminById(admin.getAdminId()) == null) {
            logger.warning("Cannot update. Admin not found: id=" + admin.getAdminId());
            throw new IllegalArgumentException("Admin not found.");
            // Checks if the admin exists before updating.
        }
        logger.info("Updating admin: id=" + admin.getAdminId());
        return adminDAO.updateAdmin(admin);
        // Delegates update operation to DAO.
    }

    public boolean deleteAdmin(int adminId) {
        if (adminDAO.getAdminById(adminId) == null) {
            logger.warning("Cannot delete. Admin not found: id=" + adminId);
            throw new IllegalArgumentException("Admin not found.");
            // Validates existence before deletion.
        }
        logger.info("Deleting admin: id=" + adminId);
        return adminDAO.deleteAdmin(adminId);
        // Deletes admin through DAO.
    }

    public Admin login(String username, String password) {
        Admin admin = adminDAO.getAdminByUsername(username);
        // Fetch admin record by username.

        if (admin != null && admin.getPassword().equals(password)) {
            logger.info("Admin login successful: " + username);
            return admin;
            // Successful login if password matches.
        }
        logger.warning("Invalid login attempt for username: " + username);
        throw new IllegalArgumentException("Invalid username or password.");
        // Throws exception for invalid login.
    }
}
