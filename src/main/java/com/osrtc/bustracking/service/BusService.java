package com.osrtc.bustracking.service; 
// Declares that this class is part of the service layer of the Bus Tracking application

import com.osrtc.bustracking.dao.*; 
// Imports all DAO (Data Access Object) classes for database operations

import com.osrtc.bustracking.model.*; 
// Imports all model classes representing entities like Bus, Driver, Route

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger; 
// Imports standard utilities for lists and logging

/**
 * Service layer for Bus operations.
 * Handles business logic and validation before delegating to the DAO layer.
 */
public class BusService {

    private static final Logger logger = Logger.getLogger(BusService.class.getName());
    // Logger to track events, warnings, and info messages for debugging

    // DAO instances to interact with database
    private BusDAO busDAO = new BusDAO();
    private RouteDAO routeDAO = new RouteDAO();
    private DriverDAO driverDAO = new DriverDAO();
    BusDAO dao = new BusDAO(); // redundant duplicate, but kept as in original code

    // Retrieve all buses from database
    public List<Bus> getAllBuses() {
        logger.info("Fetching all buses");
        return busDAO.getAllBuses();
    }

    // Retrieve a specific bus by its ID
    public Bus getBusById(int id) {
        logger.info("Fetching bus by ID: " + id);
        return busDAO.getBusById(id);
    }

    // Add a new bus with validations
    public boolean addBus(Bus bus) {
        logger.info("Adding new bus: " + bus.getBusNumber());

        // Validate required fields
        if(bus.getBusNumber() == null || bus.getRouteId() <= 0 || bus.getDriverId() <= 0) {
            logger.warning("Invalid bus data: " + bus);
            throw new IllegalArgumentException("Bus Number, Route, and Driver are required.");
        }

        // Check if route exists
        if(routeDAO.getRouteById(bus.getRouteId()) == null) {
            logger.warning("Route does not exist: " + bus.getRouteId());
            throw new IllegalArgumentException("Route does not exist.");
        }

        // Check if driver exists
        if(driverDAO.getDriverById(bus.getDriverId()) == null) {
            logger.warning("Driver does not exist: " + bus.getDriverId());
            throw new IllegalArgumentException("Driver does not exist.");
        }

        // Prevent duplicate bus numbers
        for(Bus b : busDAO.getAllBuses()) {
            if(b.getBusNumber().equalsIgnoreCase(bus.getBusNumber())) {
                logger.warning("Duplicate bus number: " + bus.getBusNumber());
                throw new IllegalArgumentException("Bus with same number already exists.");
            }
        }

        return busDAO.addBus(bus); // Delegate insertion to DAO
    }

    // Add multiple buses in bulk and return individual responses
    public List<String> multipleBusInsert(List<Bus> buses) {
        logger.info("Bulk inserting " + buses.size() + " buses");
        List<String> responses = new ArrayList<>();
        for (Bus bus : buses) {
            try {
                validateBus(bus); // Validate each bus
                busDAO.addBus(bus); // Add bus to database
                logger.info("Bus added successfully: " + bus.getBusNumber());
                responses.add("Bus Added: " + bus.getBusNumber());
            } catch (Exception e) {
                logger.warning("Failed to add bus " + bus.getBusNumber() + ": " + e.getMessage());
                responses.add("Failed: " + bus.getBusNumber() + " | Reason: " + e.getMessage());
            }
        }
        return responses;
    }

    // Validate a bus object before adding/updating
    private void validateBus(Bus bus) {
        if (bus.getBusNumber() == null || bus.getBusNumber().isEmpty())
            throw new IllegalArgumentException("Bus Number required");

        if (routeDAO.getRouteById(bus.getRouteId()) == null)
            throw new IllegalArgumentException("Invalid route: " + bus.getRouteId());

        if (driverDAO.getDriverById(bus.getDriverId()) == null)
            throw new IllegalArgumentException("Invalid driver: " + bus.getDriverId());
    }

    // Update an existing bus
    public boolean updateBus(Bus bus) {
        logger.info("Updating bus ID: " + bus.getBusId());
        if(busDAO.getBusById(bus.getBusId()) == null) { // Check if bus exists
            logger.warning("Bus not found for update: " + bus.getBusId());
            throw new IllegalArgumentException("Bus not found.");
        }
        return busDAO.updateBus(bus); // Delegate update to DAO
    }

    // Bulk update buses
    public List<Bus> bulkUpdateBuses(List<Bus> buses) throws Exception {
        logger.info("Bulk updating " + buses.size() + " buses");
        List<Bus> updated = new ArrayList<>();
        for (Bus b : buses) {
            Bus existing = busDAO.getBusById(b.getBusId());
            if (existing == null) {
                logger.warning("Bus ID not found for bulk update: " + b.getBusId());
                throw new Exception("Bus ID not found: " + b.getBusId());
            }
            busDAO.updateBus(b); // Update each bus
            updated.add(b);
        }
        return updated;
    }

    // Delete a bus by ID
    public boolean deleteBus(int busId) {
        logger.info("Deleting bus ID: " + busId);
        if(busDAO.getBusById(busId) == null) { // Ensure bus exists
            logger.warning("Bus not found for deletion: " + busId);
            throw new IllegalArgumentException("Bus not found.");
        }
        return busDAO.deleteBus(busId);
    }

    // Bulk delete buses
    public List<Bus> bulkDeleteBuses(List<Integer> busIds) throws Exception {
        logger.info("Bulk deleting " + busIds.size() + " buses");
        List<Bus> deleted = new ArrayList<>();
        for (Integer id : busIds) {
            Bus existing = busDAO.getBusById(id);
            if (existing == null) {
                logger.warning("Bus ID not found for bulk deletion: " + id);
                throw new Exception("Bus ID not found: " + id);
            }
            busDAO.deleteBus(id); // Delete bus
            deleted.add(existing); // Keep record of deleted bus
        }
        return deleted;
    }

    // Assign a driver to a bus
    public boolean assignDriverToBus(int busId, int driverId) {
        logger.info("Assigning driver ID " + driverId + " to bus ID " + busId);
        Bus bus = busDAO.getBusById(busId);
        if (bus == null) {
            logger.warning("Bus not found: " + busId);
            throw new IllegalArgumentException("Bus not found.");
        }
        if (driverDAO.getDriverById(driverId) == null) {
            logger.warning("Driver not found: " + driverId);
            throw new IllegalArgumentException("Driver not found.");
        }
        bus.setDriverId(driverId); // Update driver assignment
        return busDAO.updateBus(bus);
    }

    // Update the operational status of a bus
    public boolean updateBusStatus(int busId, String status) {
        logger.info("Updating status of bus ID " + busId + " to " + status);
        Bus bus = busDAO.getBusById(busId);
        if (bus == null) {
            logger.warning("Bus not found for status update: " + busId);
            throw new IllegalArgumentException("Bus not found.");
        }
        bus.setStatus(status); // Set new status
        return busDAO.updateBus(bus);
    }
}
