package com.osrtc.bustracking.service;
// Declares this class is part of the service layer for location operations

import com.osrtc.bustracking.dao.LocationDAO;
import com.osrtc.bustracking.dao.BusDAO;
import com.osrtc.bustracking.model.Location;
// Imports the DAOs for Location and Bus, and the Location model class

import java.util.List;
import java.util.logging.Logger;
// Imports utilities for lists and logging

/**
 * Service layer for Location operations.
 * Handles validation and business logic before interacting with the DAO.
 */
public class LocationService {

    private static final Logger logger = Logger.getLogger(LocationService.class.getName());
    // Logger for tracking operations, warnings, and errors

    private LocationDAO locationDAO = new LocationDAO();
    private BusDAO busDAO = new BusDAO();
    // DAO instances to perform database operations for Location and Bus

    // Retrieve all locations in the system
    public List<Location> getAllLocations() {
        logger.info("Fetching all locations");
        return locationDAO.getAllLocations(); // Delegate to DAO
    }

    // Retrieve all location records for a specific bus
    public List<Location> getLocationsByBus(int busId) {
        logger.info("Fetching locations for bus ID: " + busId);

        // Validate that the bus exists
        if(busDAO.getBusById(busId) == null) {
            logger.warning("Bus not found: ID " + busId);
            throw new IllegalArgumentException("Bus not found.");
        }

        return locationDAO.getLocationsByBusId(busId); // Delegate to DAO
    }

    // Get the most recent location for a specific bus
    public Location getLatestLocation(int busId) {
        logger.info("Fetching latest location for bus ID: " + busId);

        // Validate bus existence
        if(busDAO.getBusById(busId) == null) {
            logger.warning("Bus not found: ID " + busId);
            throw new IllegalArgumentException("Bus not found.");
        }

        return locationDAO.getLatestLocationByBusId(busId); // Delegate to DAO
    }

    // Add a new location record for a bus
    public boolean addLocation(Location location) {
        logger.info("Adding new location for bus ID: " + location.getBusId());

        // Ensure the bus exists before adding a location
        if(busDAO.getBusById(location.getBusId()) == null) {
            logger.warning("Cannot add location. Bus not found: ID " + location.getBusId());
            throw new IllegalArgumentException("Bus not found.");
        }

        return locationDAO.addLocation(location); // Delegate insertion to DAO
    }
}
