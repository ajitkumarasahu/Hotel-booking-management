package com.osrtc.bustracking.main;
// Package declaration – main application initializer class.

import com.osrtc.bustracking.util.GPSSimulator;
// Import GPSSimulator utility to simulate bus location updates.

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
// Import JAX-RS annotations and Application class for REST API configuration.

import java.util.logging.Logger;
// Import Logger for logging messages.

// The commented imports show alternative Jakarta EE imports that can be used:
// import jakarta.ws.rs.ApplicationPath;
// import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
// Specifies that all REST endpoints will be served under the /api path.
// Example: GET /api/buses

public class AppInitializer extends Application {
    // Extends JAX-RS Application class, making this the main configuration class for REST APIs.

    private static final Logger logger = Logger.getLogger(AppInitializer.class.getName());
    // Logger for this class.

    public AppInitializer() {
        logger.info("AppInitializer constructor called");
        // Logs when the initializer is created during application startup.

        GPSSimulator.startSimulation();
        // Starts the GPS simulation – likely spawns threads or timers to update bus locations.

        logger.info("GPS simulation started");
        // Confirms in the logs that GPS simulation is running.
    }
}
