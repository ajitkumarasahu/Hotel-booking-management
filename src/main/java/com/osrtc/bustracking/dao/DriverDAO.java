package com.osrtc.bustracking.dao;
// Package declaration for DAO classes related to bus tracking.

import com.osrtc.bustracking.model.Driver;
// Import Driver model class.

import com.osrtc.bustracking.util.DBConnection;
// Import utility class for getting database connections.

import java.sql.*;
// Import JDBC classes for database operations.

import java.util.*;
// Import utility classes like List, ArrayList.

import java.util.logging.Logger;
// Import Java logger for logging info, warnings, and errors.

public class DriverDAO {
    // DAO class to perform CRUD operations on Driver entity.

    private static final Logger logger = Logger.getLogger(DriverDAO.class.getName());
    // Logger instance for this class.

    public List<Driver> getAllDrivers() {
        logger.info("DriverDAO.getAllDrivers() called");
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT * FROM driver";
        // SQL query to fetch all drivers.

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            // Open DB connection and execute query.

            while (rs.next()) {
                Driver d = new Driver();
                d.setDriverId(rs.getInt("driver_id"));
                d.setDriverName(rs.getString("driver_name"));
                d.setPhone(rs.getString("phone"));
                d.setLicenseNumber(rs.getString("license_number"));
                list.add(d);
                // Map each row to Driver object and add to list.
            }
            logger.info("Total drivers fetched: " + list.size());

        } catch (Exception e) {
            logger.severe("Error in getAllDrivers(): " + e.getMessage());
            e.printStackTrace();
        }
        return list; // Return list of all drivers.
    }

    public Driver getDriverById(int id) {
        logger.info("DriverDAO.getDriverById() called with id=" + id);
        Driver d = null;
        String sql = "SELECT * FROM driver WHERE driver_id=?";
        // SQL query to fetch a driver by ID.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            // Set ID parameter in query.

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new Driver();
                    d.setDriverId(rs.getInt("driver_id"));
                    d.setDriverName(rs.getString("driver_name"));
                    d.setPhone(rs.getString("phone"));
                    d.setLicenseNumber(rs.getString("license_number"));
                    logger.info("Driver found: ID=" + id);
                } else {
                    logger.warning("Driver not found: ID=" + id);
                }
            }

        } catch (Exception e) {
            logger.severe("Error in getDriverById(): " + e.getMessage());
            e.printStackTrace();
        }
        return d; // Return Driver object or null if not found.
    }

    public boolean addDriver(Driver d) {
        logger.info("DriverDAO.addDriver() called for: " + d.getDriverName());
        String sql = "INSERT INTO driver(driver_name, phone, license_number) VALUES(?,?,?)";
        // SQL insert statement.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getDriverName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getLicenseNumber());
            // Set values for insert.

            int affected = ps.executeUpdate();
            logger.info("Rows affected: " + affected);

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        d.setDriverId(keys.getInt(1));
                        logger.info("Driver added successfully with ID: " + d.getDriverId());
                    }
                }
                return true;
            } else {
                logger.warning("Failed to add driver: " + d.getDriverName());
            }

        } catch (SQLException e) {
            logger.severe("SQL ERROR in addDriver(): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.severe("Error in addDriver(): " + e.getMessage());
            e.printStackTrace();
        }

        return false; // Return false if insert fails.
    }

    public boolean addMultipleDrivers(List<Driver> drivers) {
        logger.info("DriverDAO.addMultipleDrivers() called for " + drivers.size() + " drivers");
        String sql = "INSERT INTO driver(driver_name, phone, license_number) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Driver d : drivers) {
                ps.setString(1, d.getDriverName());
                ps.setString(2, d.getPhone());
                ps.setString(3, d.getLicenseNumber());
                ps.addBatch();
                // Add each driver to batch insert.
            }

            int[] results = ps.executeBatch();
            for (int res : results) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    logger.warning("One of the driver inserts failed");
                    return false;
                }
            }
            logger.info("All drivers inserted successfully");
            return true;

        } catch (Exception e) {
            logger.severe("Error in addMultipleDrivers(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateDriver(Driver d) {
        logger.info("DriverDAO.updateDriver() called for ID=" + d.getDriverId());
        String sql = "UPDATE driver SET driver_name=?, phone=?, license_number=? WHERE driver_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getDriverName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getLicenseNumber());
            ps.setInt(4, d.getDriverId());

            boolean updated = ps.executeUpdate() > 0;
            if (updated) {
                logger.info("Driver updated successfully: ID=" + d.getDriverId());
            } else {
                logger.warning("Driver not found for update: ID=" + d.getDriverId());
            }
            return updated;

        } catch (Exception e) {
            logger.severe("Error in updateDriver(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Driver> bulkUpdateDrivers(List<Driver> drivers) {
        logger.info("DriverDAO.bulkUpdateDrivers() called for " + drivers.size() + " drivers");
        List<Driver> updatedDrivers = new ArrayList<>();
        String sql = "UPDATE driver SET driver_name=?, phone=?, license_number=? WHERE driver_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Driver d : drivers) {
                ps.setString(1, d.getDriverName());
                ps.setString(2, d.getPhone());
                ps.setString(3, d.getLicenseNumber());
                ps.setInt(4, d.getDriverId());

                int affected = ps.executeUpdate();
                if (affected > 0) {
                    updatedDrivers.add(d);
                } else {
                    logger.warning("Failed to update driver: ID=" + d.getDriverId());
                }
            }

        } catch (Exception e) {
            logger.severe("Error in bulkUpdateDrivers(): " + e.getMessage());
            e.printStackTrace();
        }
        logger.info("Total drivers updated successfully: " + updatedDrivers.size());
        return updatedDrivers;
    }

    public boolean updateDriverStatus(Driver d) {
        logger.info("DriverDAO.updateDriverStatus() called for ID=" + d.getDriverId() + ", status=" + d.getStatus());
        String sql = "UPDATE driver SET status=? WHERE driver_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getStatus());
            ps.setInt(2, d.getDriverId());

            boolean updated = ps.executeUpdate() > 0;
            if (updated) {
                logger.info("Driver status updated successfully: ID=" + d.getDriverId());
            } else {
                logger.warning("Driver not found for status update: ID=" + d.getDriverId());
            }
            return updated;

        } catch (Exception e) {
            logger.severe("Error in updateDriverStatus(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteDriver(int id) {
        logger.info("DriverDAO.deleteDriver() called for ID=" + id);
        String sql = "DELETE FROM driver WHERE driver_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            boolean deleted = ps.executeUpdate() > 0;
            if (deleted) {
                logger.info("Driver deleted successfully: ID=" + id);
            } else {
                logger.warning("Driver not found for deletion: ID=" + id);
            }
            return deleted;

        } catch (Exception e) {
            logger.severe("Error in deleteDriver(): " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
