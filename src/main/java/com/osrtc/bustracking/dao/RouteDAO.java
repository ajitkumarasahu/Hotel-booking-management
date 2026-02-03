package com.osrtc.bustracking.dao;
// Package declaration – this class is part of DAO layer for bus tracking.

import com.osrtc.bustracking.model.Route;
// Import Route model class.

import com.osrtc.bustracking.util.DBConnection;
// Import utility class for database connections.

import java.sql.*;
// Import JDBC classes: Connection, PreparedStatement, ResultSet, Statement.

import java.util.*;
// Import Java utility classes: List, ArrayList.

import java.util.logging.Logger;
// Import Java Logger for logging messages.

/**
 * Data Access Object for Route entities.
 * Handles CRUD operations for the "route" table, including bulk insert and bulk update.
 */
public class RouteDAO {

    private static final Logger logger = Logger.getLogger(RouteDAO.class.getName());
    // Logger instance for this class.

    public List<Route> getAllRoutes() {
        logger.info("RouteDAO.getAllRoutes() called");
        List<Route> list = new ArrayList<>();
        String sql = "SELECT * FROM route";
        // SQL query to fetch all routes.

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Route r = new Route();
                r.setRouteId(rs.getInt("route_id"));
                r.setStartPoint(rs.getString("start_point"));
                r.setEndPoint(rs.getString("end_point"));
                r.setTotalDistanceKm(rs.getDouble("total_distance_km"));
                r.setEstimatedTime(rs.getString("estimated_time"));
                list.add(r);
                // Map each row to Route object and add to list.
            }
            logger.info("Total routes fetched: " + list.size());

        } catch (Exception e) {
            logger.severe("Error in getAllRoutes(): " + e.getMessage());
            e.printStackTrace();
        }

        return list; // Return all routes.
    }

    public Route getRouteById(int id) {
        logger.info("RouteDAO.getRouteById() called for routeId=" + id);
        Route r = null;
        String sql = "SELECT * FROM route WHERE route_id=?";
        // SQL query to fetch a route by ID.

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r = new Route();
                    r.setRouteId(rs.getInt("route_id"));
                    r.setStartPoint(rs.getString("start_point"));
                    r.setEndPoint(rs.getString("end_point"));
                    r.setTotalDistanceKm(rs.getDouble("total_distance_km"));
                    r.setEstimatedTime(rs.getString("estimated_time"));
                    logger.info("Route found: routeId=" + id);
                } else {
                    logger.warning("No route found for routeId=" + id);
                }
            }

        } catch (Exception e) {
            logger.severe("Error in getRouteById(): " + e.getMessage());
            e.printStackTrace();
        }

        return r; // Return Route or null if not found.
    }

    public boolean addRoute(Route r) {
        logger.info("RouteDAO.addRoute() called: " + r.getStartPoint() + " -> " + r.getEndPoint());
        String sql = "INSERT INTO route(start_point, end_point, total_distance_km, estimated_time) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getStartPoint());
            ps.setString(2, r.getEndPoint());
            ps.setDouble(3, r.getTotalDistanceKm());
            ps.setString(4, r.getEstimatedTime());

            int affected = ps.executeUpdate();
            logger.info("Rows affected: " + affected);

            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        r.setRouteId(keys.getInt(1));
                        logger.info("Route added successfully with ID=" + r.getRouteId());
                    }
                }
                return true;
            }

        } catch (Exception e) {
            logger.severe("Error in addRoute(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean addMultipleRoutes(List<Route> routes) {
        logger.info("RouteDAO.addMultipleRoutes() called for " + routes.size() + " routes");
        String sql = "INSERT INTO route(start_point, end_point, total_distance_km, estimated_time) VALUES(?,?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Route r : routes) {
                ps.setString(1, r.getStartPoint());
                ps.setString(2, r.getEndPoint());
                ps.setDouble(3, r.getTotalDistanceKm());
                ps.setString(4, r.getEstimatedTime());
                ps.addBatch();
                // Add each route to batch insert.
            }

            int[] results = ps.executeBatch();
            for (int res : results) {
                if (res == Statement.EXECUTE_FAILED) {
                    logger.warning("One of the routes failed to insert");
                    return false;
                }
            }
            logger.info("All routes inserted successfully");
            return true;

        } catch (Exception e) {
            logger.severe("Error in addMultipleRoutes(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateRoute(Route r) {
        logger.info("RouteDAO.updateRoute() called for routeId=" + r.getRouteId());
        String sql = "UPDATE route SET start_point=?, end_point=?, total_distance_km=?, estimated_time=? WHERE route_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getStartPoint());
            ps.setString(2, r.getEndPoint());
            ps.setDouble(3, r.getTotalDistanceKm());
            ps.setString(4, r.getEstimatedTime());
            ps.setInt(5, r.getRouteId());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                logger.info("Route updated successfully: routeId=" + r.getRouteId());
                return true;
            } else {
                logger.warning("Route update failed: routeId=" + r.getRouteId());
            }

        } catch (Exception e) {
            logger.severe("Error in updateRoute(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public List<Route> bulkUpdateRoutes(List<Route> routes) {
        logger.info("RouteDAO.bulkUpdateRoutes() called for " + routes.size() + " routes");
        List<Route> failedUpdates = new ArrayList<>();
        String sql = "UPDATE route SET start_point=?, end_point=?, total_distance_km=?, estimated_time=? WHERE route_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Route r : routes) {
                ps.setString(1, r.getStartPoint());
                ps.setString(2, r.getEndPoint());
                ps.setDouble(3, r.getTotalDistanceKm());
                ps.setString(4, r.getEstimatedTime());
                ps.setInt(5, r.getRouteId());

                int affected = ps.executeUpdate();
                if (affected == 0) {
                    logger.warning("Failed to update routeId=" + r.getRouteId());
                    failedUpdates.add(r);
                }
            }

        } catch (Exception e) {
            logger.severe("Error in bulkUpdateRoutes(): " + e.getMessage());
            e.printStackTrace();
        }

        return failedUpdates; // Return routes that failed to update.
    }

    public boolean deleteRoute(int id) {
        logger.info("RouteDAO.deleteRoute() called for routeId=" + id);
        String sql = "DELETE FROM route WHERE route_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                logger.info("Route deleted successfully: routeId=" + id);
                return true;
            } else {
                logger.warning("No route found to delete: routeId=" + id);
            }

        } catch (Exception e) {
            logger.severe("Error in deleteRoute(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteMultipleRoutes(List<Integer> routeIds) {
        logger.info("RouteDAO.deleteMultipleRoutes() called for " + routeIds.size() + " routes");
        String sql = "DELETE FROM route WHERE route_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (Integer id : routeIds) {
                ps.setInt(1, id);
                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            for (int i = 0; i < results.length; i++) {
                if (results[i] == 0) {
                    logger.warning("Failed to delete routeId=" + routeIds.get(i));
                    return false;
                }
            }
            logger.info("All routes deleted successfully");
            return true;

        } catch (Exception e) {
            logger.severe("Error in deleteMultipleRoutes(): " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
