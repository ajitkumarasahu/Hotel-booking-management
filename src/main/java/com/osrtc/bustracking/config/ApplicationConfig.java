// Package declaration: configuration classes
package com.osrtc.bustracking.config;

// Jersey ResourceConfig: allows configuration of JAX-RS application
import org.glassfish.jersey.server.ResourceConfig;

// JAX-RS annotation to define base URI for all REST endpoints
import javax.ws.rs.ApplicationPath;

// Java Logging API
import java.util.logging.Logger; // ✅ ADDED

/**
 * ApplicationConfig
 * -------------------------
 * Configures the Jersey REST application.
 * Sets the base API path and registers controller packages.
 */
@ApplicationPath("/api") // All REST endpoints will be prefixed with /api
public class ApplicationConfig extends ResourceConfig {

    // Logger instance for configuration messages
    private static final Logger logger = Logger.getLogger(ApplicationConfig.class.getName());

    /**
     * Constructor
     * Registers controller package and logs initialization
     */
    public ApplicationConfig() {
        // Scan this package for all REST controllers (e.g., BusController, AdminController)
        packages("com.osrtc.bustracking.controller");

        // Log that configuration is complete
        logger.info("ApplicationConfig initialized – REST API base path: /api");
    }
}
