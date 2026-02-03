package com.osrtc.bustracking.service;
// Declares this class is part of the service layer for route operations

import com.osrtc.bustracking.dao.RouteDAO;
import com.osrtc.bustracking.model.Route;
// Imports RouteDAO for database operations and Route model class

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
// Imports utilities for list handling and logging

/**
 * Service layer for Route operations.
 * Handles business logic and validation before delegating to RouteDAO.
 */
public class RouteService {

    private static final Logger logger = Logger.getLogger(RouteService.class.getName());
    // Logger for info, warnings, and debugging

    private RouteDAO routeDAO = new RouteDAO();
    // DAO instance to interact with the database for route operations

    // Retrieve all routes
    public List<Route> getAllRoutes() {
        logger.info("Fetching all routes");
        return routeDAO.getAllRoutes(); // Delegate to DAO
    }

    // Retrieve a specific route by its ID
    public Route getRouteById(int id) {
        logger.info("Fetching route with ID: " + id);
        return routeDAO.getRouteById(id); // Delegate to DAO
    }

    // Add a single new route with validation
    public boolean addRoute(Route route) {
        logger.info("Adding new route: " + route.getStartPoint() + " -> " + route.getEndPoint());

        // Validate required fields
        if(route.getStartPoint() == null || route.getEndPoint() == null) {
            logger.warning("Start and End points are required for adding route");
            throw new IllegalArgumentException("Start and End points are required.");
        }

        // Prevent duplicate routes
        for(Route r : routeDAO.getAllRoutes()) {
            if(r.getStartPoint().equalsIgnoreCase(route.getStartPoint()) &&
               r.getEndPoint().equalsIgnoreCase(route.getEndPoint())) {
                logger.warning("Duplicate route exists: " + route.getStartPoint() + " -> " + route.getEndPoint());
                throw new IllegalArgumentException("Route already exists.");
            }
        }

        return routeDAO.addRoute(route); // Delegate to DAO
    }

    // Add multiple routes in bulk with individual logging for success/failure
    public boolean addMultipleRoutes(List<Route> routes) {
        logger.info("Adding multiple routes: count = " + routes.size());
        boolean allSuccessful = true;

        for (Route route : routes) {
            try {
                addRoute(route); // Reuse single addRoute method with validations
                logger.info("Route added successfully: " + route.getStartPoint() + " -> " + route.getEndPoint());
            } catch (Exception e) {
                allSuccessful = false; // If any fail, mark as unsuccessful
                logger.warning("Failed to add route: " + route.getStartPoint() + " -> " + route.getEndPoint() +
                        " | Reason: " + e.getMessage());
            }
        }

        return allSuccessful; // True if all succeeded, false if any failed
    }

    // Update an existing route
    public boolean updateRoute(Route route) {
        logger.info("Updating route ID: " + route.getRouteId());

        // Validate route existence
        if(routeDAO.getRouteById(route.getRouteId()) == null) {
            logger.warning("Route not found: ID " + route.getRouteId());
            throw new IllegalArgumentException("Route not found.");
        }

        return routeDAO.updateRoute(route); // Delegate update to DAO
    }

    // Bulk update multiple routes
    public List<Route> bulkUpdateRoutes(List<Route> routes) {
        logger.info("Bulk updating routes: count = " + routes.size());
        List<Route> updatedRoutes = new ArrayList<>();

        for (Route route : routes) {
            Route existing = routeDAO.getRouteById(route.getRouteId());

            // Validate existence
            if (existing == null) {
                logger.warning("Route not found for bulk update: ID " + route.getRouteId());
                throw new IllegalArgumentException("Route not found: ID " + route.getRouteId());
            }

            routeDAO.updateRoute(route); // Update the route
            logger.info("Route updated: ID " + route.getRouteId());
            updatedRoutes.add(route); // Keep track of updated routes
        }

        return updatedRoutes; // Return list of successfully updated routes
    }

    // Delete a single route by ID
    public boolean deleteRoute(int routeId) {
        logger.info("Deleting route ID: " + routeId);

        // Validate existence
        if(routeDAO.getRouteById(routeId) == null) {
            logger.warning("Route not found: ID " + routeId);
            throw new IllegalArgumentException("Route not found.");
        }

        return routeDAO.deleteRoute(routeId); // Delegate deletion to DAO
    }

    // Bulk delete multiple routes by their IDs
    public List<Route> bulkDeleteRoutes(List<Integer> routeIds) throws Exception {
        logger.info("Bulk deleting routes: count = " + routeIds.size());
        List<Route> deletedRoutes = new ArrayList<>();

        for (Integer id : routeIds) {
            Route existing = routeDAO.getRouteById(id);

            // Validate existence
            if (existing == null) {
                logger.warning("Route ID not found for deletion: " + id);
                throw new Exception("Route ID not found: " + id);
            }

            routeDAO.deleteRoute(id); // Delete route
            logger.info("Route deleted: ID " + id);
            deletedRoutes.add(existing); // Keep track of deleted routes
        }

        return deletedRoutes; // Return list of deleted routes
    }
}
