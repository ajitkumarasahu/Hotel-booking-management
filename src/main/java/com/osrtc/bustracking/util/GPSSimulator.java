package com.osrtc.bustracking.util;

import com.osrtc.bustracking.dao.LocationDAO;
import com.osrtc.bustracking.model.Location;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simulates GPS updates for buses.
 * Periodically generates random location data and saves it to the database.
 */
public class GPSSimulator {

    // Logger for logging updates
    private static final Logger logger = Logger.getLogger(GPSSimulator.class.getName());

    // DAO to persist Location objects in the database
    private static final LocationDAO locationDAO = new LocationDAO();

    // Random number generator to simulate slight movements
    private static final Random random = new Random();

    // How often to update bus locations, in seconds
    private static final int UPDATE_INTERVAL_SECONDS = 5; // Every 5 seconds

    // List of bus IDs to simulate
    private static final List<Integer> busIds = Arrays.asList(1, 2, 3);

    // Current coordinates of each bus (latitude, longitude)
    private static final Map<Integer, double[]> busCoordinates = new HashMap<>();

    static {
        // Initialize starting coordinates for each bus
        busCoordinates.put(1, new double[]{20.2961, 85.8245}); // Bus 1
        busCoordinates.put(2, new double[]{20.2950, 85.8240}); // Bus 2
        busCoordinates.put(3, new double[]{20.2970, 85.8250}); // Bus 3
    }

    /**
     * Starts the GPS simulation.
     * Uses a ScheduledExecutorService to update bus locations periodically.
     */
    public static void startSimulation() {
        // Create a scheduler with a single thread
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the location update task at a fixed rate
        scheduler.scheduleAtFixedRate(
            GPSSimulator::updateLocations, // Task to run
            0,                             // Initial delay
            UPDATE_INTERVAL_SECONDS,       // Period between executions
            TimeUnit.SECONDS               // Time unit
        );
    }

    /**
     * Updates the location of all simulated buses.
     * Moves each bus slightly and saves the new Location in the database.
     */
    private static void updateLocations() {
        for (Integer busId : busIds) {
            try {
                // Get current coordinates
                double[] coords = busCoordinates.get(busId);

                // Simulate small random movement
                coords[0] += (random.nextDouble() - 0.5) * 0.001; // Latitude
                coords[1] += (random.nextDouble() - 0.5) * 0.001; // Longitude

                // Create Location object with updated coordinates
                Location loc = new Location();
                loc.setBusId(busId);
                loc.setLatitude(coords[0]);
                loc.setLongitude(coords[1]);
                loc.setTimestamp(new Date());

                // Persist the location in the database
                locationDAO.addLocation(loc);

                // Log the updated location
                logger.info("Updated location for Bus " + busId +
                        " -> Lat: " + coords[0] + ", Lng: " + coords[1]);

            } catch (Exception e) {
                // Log any exceptions during location update
                logger.log(Level.SEVERE, "Failed to update location for Bus " + busId, e);
            }
        }
    }
}
