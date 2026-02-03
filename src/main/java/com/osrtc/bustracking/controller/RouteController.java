// Package declaration: defines the controller package
package com.osrtc.bustracking.controller;

// Import DAO class for route database operations
import com.osrtc.bustracking.dao.RouteDAO;

// Import Route model class (represents route entity)
import com.osrtc.bustracking.model.Route;

// JAX-RS imports for REST API annotations and response handling
import javax.ws.rs.*;              // REST annotations like @GET, @POST, @PUT, @DELETE
import javax.ws.rs.core.*;         // Response, MediaType, UriInfo, UriBuilder, etc.

// Java utility imports
import java.util.List;             // Used to handle list of routes
import java.util.logging.Logger;   // Used for logging API activities

/**
 * RouteController
 * -------------------------
 * REST Controller for managing Route-related APIs.
 * Base URL: /route
 */
@Path("/route")                              // Base path for route APIs
@Produces(MediaType.APPLICATION_JSON)      // Response format: JSON
@Consumes(MediaType.APPLICATION_JSON)      // Request format: JSON
public class RouteController {

    // Logger instance for logging controller activities
    private static final Logger logger = Logger.getLogger(RouteController.class.getName());

    // DAO instance responsible for route persistence
    private RouteDAO dao = new RouteDAO();

    /**
     * GET /route
     * Fetch all routes from database
     */
    @GET
    public List<Route> getAll() { 
        logger.info("GET /route - Fetch all routes"); // Log API call

        // Call DAO method to fetch all routes
        List<Route> routes = dao.getAllRoutes();

        // Log number of routes fetched
        logger.info("Total routes fetched: " + (routes != null ? routes.size() : 0));

        // Return routes list as JSON
        return routes; 
    }

    /**
     * GET /route/{id}
     * Fetch route by ID
     */
    @GET
    @Path("/{id}")
    public Response getRouteById(@PathParam("id") int id) {

        logger.info("GET /route/" + id + " - Fetch route by ID");

        // Fetch route from DAO
        Route r = dao.getRouteById(id);

        // If route not found, return 404
        if (r == null){
            logger.warning("Route not found with ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Route not found\"}")
                    .build();
        } 
        // If route found, return 200 OK with route data
        else {
            logger.info("Route found with ID: " + id);
            return Response.ok(r).build();
        }
    }

    /**
     * POST /route
     * Add a new route
     */
    @POST
    public Response add(Route r, @Context UriInfo uriInfo) {

        logger.info("POST /route - Add route request received");

        // Insert route into database using DAO
        if (dao.addRoute(r)) {
            logger.info("Route added successfully. RouteId: " + r.getRouteId());

            // Build URI for newly created route resource
            UriBuilder builder = uriInfo.getAbsolutePathBuilder()
                                        .path(Integer.toString(r.getRouteId()));

            // Return 201 CREATED with route object
            return Response.created(builder.build())
                           .entity(r)
                           .build();
        } 
        // If insert fails, return 400 BAD REQUEST
        else {
            logger.warning("Route insert failed");
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"message\":\"Route insert failed\"}")
                           .build();
        } 
    }

    /**
     * POST /route/multipleroutes
     * Add multiple routes in bulk
     */
    @POST
    @Path("/multipleroutes")
    public Response add(List<Route> routes) {

        logger.info("POST /route/multipleroutes - Bulk add routes request");
        logger.info("Total routes received: " + (routes != null ? routes.size() : 0));

        // Call DAO method to insert multiple routes
        boolean result = dao.addMultipleRoutes(routes);

        if (result) {
            logger.info("Multiple routes added successfully");
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Routes Added Successfully\"}")
                    .build();
        } 
        else {
            logger.warning("Multiple route insert failed");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Route Insert Failed\"}")
                    .build();
        }
    }

    /**
     * PUT /route/{id}
     * Update route by ID
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Route r) {

        logger.info("PUT /route/" + id + " - Update route request");

        // Set route ID from path parameter
        r.setRouteId(id);

        // Update route using DAO
        if (dao.updateRoute(r)) {
            logger.info("Route updated successfully. ID: " + id);
            return Response.ok()
                    .entity("{\"message\":\"Route updated successfully\"}")
                    .build();
        } 
        else {
            logger.warning("Route update failed. ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Update Failed\"}")
                    .build();
        } 
    }

    /**
     * PUT /route/bulk
     * Update multiple routes at once
     */
    @PUT
    @Path("/bulk")
    public Response bulkUpdateRoutes(List<Route> routes) {

        logger.info("PUT /route/bulk - Bulk update routes request");

        // DAO returns list of routes that failed to update
        List<Route> failedUpdates = dao.bulkUpdateRoutes(routes);

        // If no failures, all updates succeeded
        if (failedUpdates.isEmpty()) {
            logger.info("Bulk update successful for all routes");
            return Response.ok("{\"message\":\"All routes updated successfully\"}").build();
        } 
        // If some updates failed
        else {
            logger.warning("Bulk update failed for some routes. Failed count: " + failedUpdates.size());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Bulk update failed for some routes\"}")
                    .build();
        }
    }

    /**
     * DELETE /route/{id}
     * Delete route by ID
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {

        logger.info("DELETE /route/" + id + " - Delete route request");

        // Delete route using DAO
        if (dao.deleteRoute(id)){
            logger.info("Route deleted successfully. ID: " + id);
            return Response.ok("{\"message\":\"Route deleted successfully\"}").build();
        } 
        else {
            logger.warning("Route not found for deletion. ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Route not found\"}")
                    .build();
        }
    }

    /**
     * DELETE /route/bulk
     * Delete multiple routes by IDs
     */
    @DELETE
    @Path("/bulk")
    public Response bulkDeleteRoutes(List<Integer> routeIds) {

        logger.info("DELETE /route/bulk - Bulk delete routes request");

        try {
            // Loop through each route ID and delete
            for (Integer id : routeIds) {
                logger.info("Deleting route ID: " + id);

                if (!dao.deleteRoute(id)) {
                    logger.warning("Failed to delete route ID: " + id);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Failed to delete route with ID: " + id)
                            .build();         
                }
            }

            logger.info("All routes deleted successfully");
            return Response.ok("{\"message\":\"All routes deleted successfully\"}").build();
        } 
        catch (Exception e) {
            logger.severe("Bulk route deletion failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();    
        }
    }
}
