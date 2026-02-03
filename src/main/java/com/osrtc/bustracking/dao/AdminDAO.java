// Package declaration: DAO layer for Admin entity
package com.osrtc.bustracking.dao;

// Admin model and database connection utility
import com.osrtc.bustracking.model.Admin;
import com.osrtc.bustracking.util.DBConnection;

// Java SQL imports
import java.sql.*;
import java.util.*;

// Logger for monitoring DAO operations
import java.util.logging.Logger; // ✅ ADDED

/**
 * AdminDAO
 * -------------------------
 * Handles all CRUD operations for the Admin entity.
 * Uses JDBC and DBConnection utility.
 */
public class AdminDAO {

    // Logger instance
    private static final Logger logger = Logger.getLogger(AdminDAO.class.getName()); // ✅ ADDED

    /**
     * Fetch all admins
     */
    public List<Admin> getAllAdmins() {
        logger.info("AdminDAO.getAllAdmins() called");

        List<Admin> list = new ArrayList<>();
        String sql = "SELECT * FROM admin";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Admin a = new Admin();
                a.setAdminId(rs.getInt("admin_id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                list.add(a);
            }

            logger.info("Total admins fetched: " + list.size());

        } catch (Exception e) {
            logger.severe("Error in getAllAdmins(): " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Fetch admin by ID
     */
    public Admin getAdminById(int id) {
        logger.info("AdminDAO.getAdminById() called with id=" + id);

        Admin a = null;
        String sql = "SELECT * FROM admin WHERE admin_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = new Admin();
                    a.setAdminId(rs.getInt("admin_id"));
                    a.setUsername(rs.getString("username"));
                    a.setPassword(rs.getString("password"));
                    logger.info("Admin found: ID=" + id);
                } else {
                    logger.warning("Admin not found: ID=" + id);
                }
            }

        } catch (Exception e) {
            logger.severe("Error in getAdminById(): " + e.getMessage());
            e.printStackTrace();
        }

        return a;
    }

    /**
     * Fetch admin by username
     */
    public Admin getAdminByUsername(String username) {
        logger.info("AdminDAO.getAdminByUsername() called with username=" + username);

        Admin a = null;
        String sql = "SELECT * FROM admin WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = new Admin();
                    a.setAdminId(rs.getInt("admin_id"));
                    a.setUsername(rs.getString("username"));
                    a.setPassword(rs.getString("password"));
                    logger.info("Admin found: username=" + username);
                } else {
                    logger.warning("Admin not found: username=" + username);
                }
            }

        } catch (Exception e) {
            logger.severe("Error in getAdminByUsername(): " + e.getMessage());
            e.printStackTrace();
        }

        return a;
    }

    /**
     * Add a new admin
     */
    public boolean addAdmin(Admin a) {
        logger.info("AdminDAO.addAdmin() called for username=" + a.getUsername());

        String sql = "INSERT INTO admin(username, password) VALUES(?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());

            int affected = ps.executeUpdate();

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        a.setAdminId(keys.getInt(1));
                    }
                }
                logger.info("Admin added successfully: ID=" + a.getAdminId());
            }

            return affected > 0;

        } catch (Exception e) {
            logger.severe("Error in addAdmin(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Update an existing admin
     */
    public boolean updateAdmin(Admin a) {
        logger.info("AdminDAO.updateAdmin() called for ID=" + a.getAdminId());

        String sql = "UPDATE admin SET username=?, password=? WHERE admin_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, a.getUsername());
            ps.setString(2, a.getPassword());
            ps.setInt(3, a.getAdminId());

            boolean updated = ps.executeUpdate() > 0;
            if (updated) {
                logger.info("Admin updated successfully: ID=" + a.getAdminId());
            } else {
                logger.warning("Admin not found for update: ID=" + a.getAdminId());
            }
            return updated;

        } catch (Exception e) {
            logger.severe("Error in updateAdmin(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Delete admin by ID
     */
    public boolean deleteAdmin(int id) {
        logger.info("AdminDAO.deleteAdmin() called for ID=" + id);

        String sql = "DELETE FROM admin WHERE admin_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean deleted = ps.executeUpdate() > 0;

            if (deleted) {
                logger.info("Admin deleted successfully: ID=" + id);
            } else {
                logger.warning("Admin not found for deletion: ID=" + id);
            }

            return deleted;

        } catch (Exception e) {
            logger.severe("Error in deleteAdmin(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
