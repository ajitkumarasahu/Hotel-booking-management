// Package declaration: defines the package for controller classes
package com.osrtc.bustracking.controller;

// Import DAO class for database operations related to Location
import com.osrtc.bustracking.dao.LocationDAO;

// Import Location model class (represents location entity)
import com.osrtc.bustracking.model.Location;

// JAX-RS imports for REST API annotations and response handling
import javax.ws.rs.*;              // REST annotations like @GET, @POST, @Path, etc.
import javax.ws.rs.core.*;         // Response, MediaType, UriInfo, UriBuilder, etc.

// Java utility imports
import java.util.List;             // Used to handle list of locations
import java.util.logging.Logger;   // Used for logging API activity

/**
 * LocationController
 * -------------------------
 * REST Controller for handling Location-related APIs.
 * Base URL: /location
 */
@Path("/location")                              // Base path for all location APIs
@Produces(MediaType.APPLICATION_JSON)          // Response format: JSON
@Consumes(MediaType.APPLICATION_JSON)          // Request format: JSON
public class LocationController {

    // Logger instance for logging controller activities
    private static final Logger logger = Logger.getLogger(LocationController.class.getName());

    // DAO instance for database operations related to locations
    private LocationDAO dao = new LocationDAO();

    /**
     * GET /location
     * Fetch all locations from database
     */
    @GET
    public List<Location> getAll() {
        logger.info("GET /location - Fetch all locations"); // Log API call

        // Call DAO method to fetch all locations
        List<Location> locations = dao.getAllLocations();

        // Log number of locations fetched
        logger.info("Total locations fetched: " + (locations != null ? locations.size() : 0));

        // Return list of locations as JSON
        return locations;
    }

    /**
     * GET /location/bus/{busId}
     * Fetch all locations for a specific bus
     */
    @GET
    @Path("/bus/{busId}")
    public List<Location> getByBus(@PathParam("busId") int busId) {
        logger.info("GET /location/bus/" + busId + " - Fetch locations by busId");

        // Call DAO method to fetch locations by busId
        List<Location> locations = dao.getLocationsByBusId(busId);

        // Log number of locations fetched for the bus
        logger.info("Total locations fetched for busId " + busId + ": " +
                (locations != null ? locations.size() : 0));

        // Return list of locations as JSON
        return locations;
    }

    /**
     * GET /location/bus/{busId}/latest
     * Fetch latest location of a bus
     */
    @GET
    @Path("/bus/{busId}/latest")
    public Response getLatestByBus(@PathParam("busId") int busId) {

        logger.info("GET /location/bus/" + busId + "/latest - Fetch latest location");

        // Call DAO method to fetch latest location of the bus
        Location l = dao.getLatestLocationByBusId(busId);

        // If no location found, return 404 NOT FOUND
        if (l == null) {
            logger.warning("No latest location found for busId: " + busId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // If location found, return it with 200 OK
        logger.info("Latest location found for busId: " + busId);
        return Response.ok(l).build();
    }

    /**
     * POST /location
     * Add a new location record
     */
    @POST
    public Response add(Location loc, @Context UriInfo ui) {

        logger.info("POST /location - Add location request received");

        // Call DAO method to insert location into database
        if (dao.addLocation(loc)) {
            logger.info("Location added successfully. LocationId: " + loc.getLocationId());

            // Build URI for newly created location resource
            UriBuilder b = ui.getAbsolutePathBuilder()
                             .path(Integer.toString(loc.getLocationId()));

            // Return 201 CREATED response with location data
            return Response.created(b.build())
                           .entity(loc)
                           .build();
        }

        // If insert fails, return 400 BAD REQUEST
        logger.warning("Location insert failed");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
