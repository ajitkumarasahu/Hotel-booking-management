package com.osrtc.bustracking.dao;
// Package declaration – this class is part of the DAO layer for bus tracking.

import com.osrtc.bustracking.model.Location;
// Import Location model class.

import com.osrtc.bustracking.util.DBConnection;
// Import utility class for database connection.

import java.sql.*;
// Import JDBC classes: Connection, PreparedStatement, ResultSet, etc.

import java.util.*;
// Import Java utility classes: List, ArrayList.

import java.util.logging.Logger;
// Import Java Logger for logging messages.

/**
 * Data Access Object for Location entities.
 * Handles CRUD operations for the "location" table, including retrieval by bus and latest location.
 */
public class LocationDAO {

    private static final Logger logger = Logger.getLogger(LocationDAO.class.getName());
    // Logger instance for this class.

    public boolean addLocation(Location loc) {
        logger.info("LocationDAO.addLocation() called for busId=" + loc.getBusId());
        String sql = "INSERT INTO location(bus_id, latitude, longitude) VALUES(?,?,?)";
        // SQL query to insert a new location record for a bus.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Open DB connection and prepare statement, requesting generated keys (auto-increment ID).

            ps.setInt(1, loc.getBusId());
            ps.setDouble(2, loc.getLatitude());
            ps.setDouble(3, loc.getLongitude());
            // Set parameters for insert.

            int affected = ps.executeUpdate();
            logger.info("Rows affected: " + affected);
            // Execute insert and log number of rows affected.

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        loc.setLocationId(keys.getInt(1));
                        logger.info("Location added successfully with ID=" + loc.getLocationId());
                    }
                }
                return true;
            } else {
                logger.warning("Failed to add location for busId=" + loc.getBusId());
            }

        } catch (Exception e) {
            logger.severe("Error in addLocation(): " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Return false if insert fails.
    }

    public List<Location> getLocationsByBusId(int busId) {
        logger.info("LocationDAO.getLocationsByBusId() called for busId=" + busId);
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM location WHERE bus_id=? ORDER BY timestamp DESC";
        // SQL query to fetch all locations for a specific bus, newest first.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Location l = new Location();
                    l.setLocationId(rs.getInt("location_id"));
                    l.setBusId(rs.getInt("bus_id"));
                    l.setLatitude(rs.getDouble("latitude"));
                    l.setLongitude(rs.getDouble("longitude"));
                    l.setTimestamp(rs.getTimestamp("timestamp"));
                    list.add(l);
                    // Map each row to Location object and add to list.
                }
            }
            logger.info("Total locations fetched for busId " + busId + ": " + list.size());

        } catch (Exception e) {
            logger.severe("Error in getLocationsByBusId(): " + e.getMessage());
            e.printStackTrace();
        }

        return list; // Return list of locations.
    }

    public Location getLatestLocationByBusId(int busId) {
        logger.info("LocationDAO.getLatestLocationByBusId() called for busId=" + busId);
        Location l = null;
        String sql = "SELECT * FROM location WHERE bus_id=? ORDER BY timestamp DESC LIMIT 1";
        // SQL query to fetch the most recent location for a bus.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, busId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    l = new Location();
                    l.setLocationId(rs.getInt("location_id"));
                    l.setBusId(rs.getInt("bus_id"));
                    l.setLatitude(rs.getDouble("latitude"));
                    l.setLongitude(rs.getDouble("longitude"));
                    l.setTimestamp(rs.getTimestamp("timestamp"));
                    logger.info("Latest location found for busId=" + busId + ": locationId=" + l.getLocationId());
                } else {
                    logger.warning("No location found for busId=" + busId);
                }
            }

        } catch (Exception e) {
            logger.severe("Error in getLatestLocationByBusId(): " + e.getMessage());
            e.printStackTrace();
        }

        return l; // Return latest Location or null if none found.
    }

    public List<Location> getAllLocations() {
        logger.info("LocationDAO.getAllLocations() called");
        List<Location> list = new ArrayList<>();
        String sql = "SELECT * FROM location ORDER BY timestamp DESC";
        // SQL query to fetch all locations, newest first.

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Location l = new Location();
                l.setLocationId(rs.getInt("location_id"));
                l.setBusId(rs.getInt("bus_id"));
                l.setLatitude(rs.getDouble("latitude"));
                l.setLongitude(rs.getDouble("longitude"));
                l.setTimestamp(rs.getTimestamp("timestamp"));
                list.add(l);
                // Map each row to Location object.
            }
            logger.info("Total locations fetched: " + list.size());

        } catch (Exception e) {
            logger.severe("Error in getAllLocations(): " + e.getMessage());
            e.printStackTrace();
        }

        return list; // Return list of all locations.
    }
}
