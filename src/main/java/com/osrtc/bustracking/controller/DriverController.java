// Package declaration: defines the package where this controller belongs
package com.osrtc.bustracking.controller;

// Import DAO class for database operations related to Driver
import com.osrtc.bustracking.dao.DriverDAO;

// Import Driver model class (represents driver entity)
import com.osrtc.bustracking.model.Driver;

// JAX-RS imports for REST API annotations and response handling
import javax.ws.rs.*;              // Contains REST annotations like @GET, @POST, @PUT, @DELETE
import javax.ws.rs.core.*;         // Contains Response, MediaType, UriInfo, etc.

// Java utility imports
import java.util.List;             // Used to handle list of drivers
import java.util.logging.Logger;   // Used for logging API activities

/**
 * DriverController
 * -------------------------
 * REST Controller for handling Driver-related APIs.
 * Base URL: /driver
 */
@Path("/driver")                                  // Base path for all driver APIs
@Produces(MediaType.APPLICATION_JSON)            // Response format: JSON
@Consumes(MediaType.APPLICATION_JSON)            // Request format: JSON
public class DriverController {

    // Logger instance to log controller activities
    private static final Logger logger = Logger.getLogger(DriverController.class.getName());

    // DAO object to interact with database
    private DriverDAO dao = new DriverDAO();

    /**
     * GET /driver
     * Fetch all drivers from database
     */
    @GET
    public List<Driver> getAll() { 
        logger.info("GET /driver - Fetch all drivers"); // Log API call

        // Call DAO method to get all drivers
        List<Driver> drivers = dao.getAllDrivers();

        // Log number of drivers fetched
        logger.info("Total drivers fetched: " + (drivers != null ? drivers.size() : 0));

        // Return list of drivers as JSON
        return drivers;
    }

    /**
     * GET /driver/{id}
     * Fetch a driver by ID
     */
    @GET
    @Path("/{id}")
    public Response getDriverById(@PathParam("id") int id) {

        logger.info("GET /driver/" + id + " - Fetch driver by ID"); // Log API call

        // Fetch driver from database using DAO
        Driver d = dao.getDriverById(id);

        // If driver not found, return 404 response
        if (d == null) {
            logger.warning("Driver not found with ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Driver not found\"}")
                    .build();        
        } 
        // If driver found, return driver details
        else {
            logger.info("Driver found with ID: " + id);
            return Response.ok(d).build();
        }
    }

    /**
     * POST /driver
     * Add a new driver
     */
    @POST
    public Response add(Driver d, @Context UriInfo ui) {

        logger.info("POST /driver - Add driver request received"); // Log API call

        // Call DAO to insert driver into database
        if (dao.addDriver(d)) {
            logger.info("Driver added successfully. ID: " + d.getDriverId());

            // Build URI for newly created driver resource
            UriBuilder b = ui.getAbsolutePathBuilder()
                             .path(Integer.toString(d.getDriverId()));

            // Return 201 CREATED response
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Driver added successfully\"}")
                    .build();
        } 
        // If insert fails, return BAD REQUEST
        else {
            logger.warning("Driver insert failed");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Driver Insert Failed\"}")
                    .build();   
        }
    }

    /**
     * POST /driver/multipledriver
     * Add multiple drivers in bulk
     */
    @POST
    @Path("/multipledriver") 
    public Response add(List<Driver> drivers) {

        logger.info("POST /driver/multipledriver - Bulk add request");
        logger.info("Total drivers received: " + (drivers != null ? drivers.size() : 0));

        // Call DAO method to insert multiple drivers
        boolean result = dao.addMultipleDrivers(drivers);

        // If successful, return CREATED response
        if (result) {
            logger.info("Multiple drivers added successfully");
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Drivers Added Successfully\"}")
                    .build();
        } 
        // If failed, return BAD REQUEST
        else {
            logger.warning("Multiple driver insert failed");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Driver Insert Failed\"}")
                    .build();
        }
    }

    /**
     * PUT /driver/{id}
     * Update driver details by ID
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Driver d) {

        logger.info("PUT /driver/" + id + " - Update driver request");

        // Set driver ID from path parameter
        d.setDriverId(id);

        // Call DAO to update driver
        if (dao.updateDriver(d)){
            logger.info("Driver updated successfully. ID: " + id);
            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"Driver updated successfully!\"}")
                    .build();       
        } 
        // If update fails
        else {
            logger.warning("Driver update failed. ID: " + id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Driver update failed!\"}")
                    .build();          
        }
    }

    /**
     * PUT /driver/updateStatus/{id}
     * Update only driver status
     */
    @PUT
    @Path("/updateStatus/{id}")
    public Response updateStatus(@PathParam("id") int id, Driver d) {

        logger.info("PUT /driver/updateStatus/" + id + " - Update status request");

        // Set driver ID
        d.setDriverId(id);

        // Call DAO to update driver status
        if (dao.updateDriverStatus(d)){
            logger.info("Driver status updated successfully. ID: " + id);
            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"Driver status updated successfully!\"}")
                    .build();        
        } 
        // If update fails
        else {
            logger.warning("Driver status update failed. ID: " + id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Driver status update failed!\"}")
                    .build();           
        }
    }

    /**
     * PUT /driver/bulk
     * Update multiple drivers at once
     */
    @PUT
    @Path("/bulk")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response bulkUpdateDrivers(List<Driver> drivers) {

        logger.info("PUT /driver/bulk - Bulk update request");

        try {
            // Call DAO to update multiple drivers
            List<Driver> updatedDrivers = dao.bulkUpdateDrivers(drivers);

            logger.info("Bulk update successful. Updated count: " + 
                        (updatedDrivers != null ? updatedDrivers.size() : 0));

            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"Bulk update successful.\"}")
                    .build();          
        } 
        // Handle exceptions
        catch (Exception e) {
            logger.severe("Bulk update failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Bulk update failed: " + e.getMessage() + "\"}")
                    .build();           
        }
    }

    /**
     * DELETE /driver/{id}
     * Delete driver by ID
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {

        logger.info("DELETE /driver/" + id + " - Delete request");

        // Call DAO to delete driver
        if (dao.deleteDriver(id)) {
            logger.info("Driver deleted successfully. ID: " + id);
            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"Driver deleted successfully!\"}")
                    .build();
        } 
        // If deletion fails
        else {          
            logger.warning("Driver deletion failed. ID: " + id);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Driver deletion failed!\"}")
                    .build();
        }
    }

    /**
     * DELETE /driver/bulk
     * Delete multiple drivers by IDs
     */
    @DELETE
    @Path("/bulk")
    public Response bulkDeleteDrivers(List<Integer> driverIds) {

        logger.info("DELETE /driver/bulk - Bulk delete request");

        try {
            // Loop through each driver ID and delete
            for (Integer id : driverIds) {
                logger.info("Deleting driver ID: " + id);

                if (!dao.deleteDriver(id)) {
                    logger.warning("Failed to delete driver ID: " + id);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Failed to delete driver with ID: " + id)
                            .build();         
                }
            }

            logger.info("All drivers deleted successfully");
            return Response.status(Response.Status.OK)
                    .entity("{\"message\":\"All drivers deleted successfully.\"}")
                    .build();
        } 
        // Handle exceptions
        catch (Exception e) {
            logger.severe("Bulk deletion failed: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Bulk deletion failed: " + e.getMessage() + "\"}")
                    .build();       
        }
    }
}
