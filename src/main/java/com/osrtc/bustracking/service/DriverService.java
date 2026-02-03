package com.osrtc.bustracking.service;
// Declares this class is part of the service layer for driver operations

import com.osrtc.bustracking.dao.DriverDAO;
import com.osrtc.bustracking.model.Driver;
// Imports the DAO for database operations and the Driver model class

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
// Imports utilities for list handling and logging

/**
 * Service layer for Driver operations.
 * Handles business logic and validation before calling the DAO layer.
 */
public class DriverService {

    private static final Logger logger = Logger.getLogger(DriverService.class.getName());
    // Logger to track information, warnings, and errors

    private DriverDAO driverDAO = new DriverDAO();
    // DAO instance to perform database operations for Driver

    // Retrieve all drivers from the database
    public List<Driver> getAllDrivers() {
        logger.info("Fetching all drivers");
        return driverDAO.getAllDrivers();
    }

    // Retrieve a specific driver by their ID
    public Driver getDriverById(int id) {
        logger.info("Fetching driver by ID: " + id);
        return driverDAO.getDriverById(id);
    }

    // Add a single new driver with basic validation
    public boolean addDriver(Driver driver) {
        logger.info("Adding new driver: " + driver.getDriverName());

        // Validate required fields
        if(driver.getDriverName() == null || driver.getLicenseNumber() == null) {
            logger.warning("Driver Name or License Number missing: " + driver);
            throw new IllegalArgumentException("Driver Name and License Number are required.");
        }

        return driverDAO.addDriver(driver); // Delegate insertion to DAO
    }

    // Bulk insert multiple drivers with individual success/failure reporting
    public List<String> addMultipleDrivers(List<Driver> drivers) {
        logger.info("Bulk adding " + drivers.size() + " drivers");
        List<String> responses = new ArrayList<>();

        for (Driver driver : drivers) {
            try {
                // Validate required fields for each driver
                if(driver.getDriverName() == null || driver.getLicenseNumber() == null) {
                    logger.warning("Driver missing required fields: " + driver);
                    throw new IllegalArgumentException("Driver Name and License Number are required.");
                }
                driverDAO.addDriver(driver); // Add driver to database
                logger.info("Driver added successfully: " + driver.getDriverName());
                responses.add("Driver Added: " + driver.getDriverName());
            } catch (Exception e) {
                logger.warning("Failed to add driver " + driver.getDriverName() + ": " + e.getMessage());
                responses.add("Failed: " + driver.getDriverName() + " | Reason: " + e.getMessage());
            }
        }

        return responses; // Return list of results for each driver
    }

    // Update a single driver
    public boolean updateDriver(Driver driver) {
        logger.info("Updating driver ID: " + driver.getDriverId());

        // Check if the driver exists before updating
        if(driverDAO.getDriverById(driver.getDriverId()) == null) {
            logger.warning("Driver not found for update: " + driver.getDriverId());
            throw new IllegalArgumentException("Driver not found.");
        }

        return driverDAO.updateDriver(driver); // Delegate update to DAO
    }

    // Update only the status of a driver
    public boolean updateDriverStatus(Driver driver) {
        logger.info("Updating status for driver ID: " + driver.getDriverId());

        if(driverDAO.getDriverById(driver.getDriverId()) == null) {
            logger.warning("Driver not found for status update: " + driver.getDriverId());
            throw new IllegalArgumentException("Driver not found.");
        }

        return driverDAO.updateDriverStatus(driver); // Update driver status in DAO
    }

    // Bulk update multiple drivers
    public List<Driver> bulkUpdateDrivers(List<Driver> drivers) throws Exception {
        logger.info("Bulk updating " + drivers.size() + " drivers");
        List<Driver> updated = new ArrayList<>();

        for (Driver driver : drivers) {
            // Check if driver exists
            if(driverDAO.getDriverById(driver.getDriverId()) == null) {
                logger.warning("Driver not found for bulk update: " + driver.getDriverId());
                throw new IllegalArgumentException("Driver with ID " + driver.getDriverId() + " not found.");
            }

            boolean success = driverDAO.updateDriver(driver); // Update each driver

            if(success) {
                logger.info("Driver updated successfully: ID " + driver.getDriverId());
                updated.add(driver); // Add to updated list
            } else {
                logger.warning("Failed to update driver ID: " + driver.getDriverId());
                throw new Exception("Failed to update driver with ID " + driver.getDriverId());
            }
        }

        return updated; // Return list of successfully updated drivers
    }

    // Delete a driver by ID
    public boolean deleteDriver(int id) {
        logger.info("Deleting driver ID: " + id);

        // Ensure the driver exists before deletion
        if(driverDAO.getDriverById(id) == null) {
            logger.warning("Driver not found for deletion: " + id);
            throw new IllegalArgumentException("Driver not found.");
        }

        return driverDAO.deleteDriver(id); // Delegate deletion to DAO
    }
}
