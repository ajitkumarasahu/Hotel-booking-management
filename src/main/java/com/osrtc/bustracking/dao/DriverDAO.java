package com.osrtc.bustracking.dao;

import com.osrtc.bustracking.model.Driver;
import com.osrtc.bustracking.util.DBConnection;

import java.sql.*;
import java.util.*;

import javax.ws.rs.core.Response;

/**
 * Data Access Object for Driver entities.
 * Handles CRUD operations, batch operations, and status updates for the "driver" table.
 */
public class DriverDAO {

    /**
     * Retrieve all drivers from the database.
     * @return List of Driver objects
     */
    public List<Driver> getAllDrivers() {
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT * FROM driver";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Driver d = new Driver();
                d.setDriverId(rs.getInt("driver_id"));
                d.setDriverName(rs.getString("driver_name"));
                d.setPhone(rs.getString("phone"));
                d.setLicenseNumber(rs.getString("license_number"));
                list.add(d);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Retrieve a driver by ID.
     * @param id Driver ID
     * @return Driver object or null if not found
     */
    public Driver getDriverById(int id) {
        Driver d = null;
        String sql = "SELECT * FROM driver WHERE driver_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    d = new Driver();
                    d.setDriverId(rs.getInt("driver_id"));
                    d.setDriverName(rs.getString("driver_name"));
                    d.setPhone(rs.getString("phone"));
                    d.setLicenseNumber(rs.getString("license_number"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * Insert a new driver into the database.
     * @param d Driver object
     * @return true if insertion succeeded
     */
    public boolean addDriver(Driver d) {

        System.out.println("Driver received: " + d.getDriverName());

        String sql = "INSERT INTO driver(driver_name, phone, license_number) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, d.getDriverName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getLicenseNumber());

            int affected = ps.executeUpdate();

            System.out.println("Rows affected: " + affected);

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        d.setDriverId(keys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            // 🔥 THIS is what you were missing
            System.err.println("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Insert multiple drivers in batch.
     * @param drivers List of Driver objects
     * @return true if all inserts succeeded
     */
    public boolean addMultipleDrivers(List<Driver> drivers) {
        String sql = "INSERT INTO driver(driver_name, phone, license_number) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Driver d : drivers) {
                ps.setString(1, d.getDriverName());
                ps.setString(2, d.getPhone());
                ps.setString(3, d.getLicenseNumber());
                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            for (int res : results) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    return false; // if any insert fails
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update a driver's details.
     * @param d Driver object with updated info
     * @return true if update succeeded
     */
    public boolean updateDriver(Driver d) {
        String sql = "UPDATE driver SET driver_name=?, phone=?, license_number=? WHERE driver_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getDriverName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getLicenseNumber());
            ps.setInt(4, d.getDriverId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Bulk update multiple drivers.
     * @param drivers List of Driver objects
     * @return List of successfully updated drivers
     */
    public List<Driver> bulkUpdateDrivers(List<Driver> drivers) {
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
                    throw new Exception("Failed to update driver with ID " + d.getDriverId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updatedDrivers;
    }

    /**
     * Update a driver's status.
     * @param d Driver object with status
     * @return true if update succeeded
     */
    public boolean updateDriverStatus(Driver d) {
        String sql = "UPDATE driver SET status=? WHERE driver_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, d.getStatus());
            ps.setInt(2, d.getDriverId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete a driver by ID.
     * @param id Driver ID
     * @return true if deletion succeeded
     */
    public boolean deleteDriver(int id) {
        String sql = "DELETE FROM driver WHERE driver_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
