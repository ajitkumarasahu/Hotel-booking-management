package com.osrtc.bustracking.validators;
// Declares that this class is part of the validators package

import com.osrtc.bustracking.dao.BusDAO;
import com.osrtc.bustracking.model.Bus;
// Imports the DAO to access bus records and the Bus model class

import java.util.List;
import java.util.logging.Logger;
// Imports utilities for lists and logging

/**
 * Validator class to ensure bus numbers are unique.
 * This class checks that a bus number does not already exist in the system before adding a new bus.
 */
public class BusnumberValidator {

    private static final Logger logger = Logger.getLogger(BusnumberValidator.class.getName());
    // Logger to track validation process, info, and warnings

    private BusDAO busDAO = new BusDAO();
    // DAO instance to interact with the bus database table

    /**
     * Validates that a bus number is not already present in the database.
     *
     * @param busNumber The bus number to validate
     * @throws Exception If a bus with the same number already exists
     */
    public void validateDuplicateBusNumber(String busNumber) throws Exception {
        logger.info("Validating duplicate bus number: " + busNumber);

        // Retrieve all buses from the database
        List<Bus> allBuses = busDAO.getAllBuses();

        // Loop through all buses to check if the bus number already exists
        for (Bus b : allBuses) {
            if (b.getBusNumber().equalsIgnoreCase(busNumber)) { // Case-insensitive check
                logger.warning("Duplicate bus number found: " + busNumber);
                throw new Exception("Bus with number " + busNumber + " already exists"); // Throw exception if duplicate
            }
        }

        // If no duplicates are found
        logger.info("Bus number is unique: " + busNumber);
    }
}
