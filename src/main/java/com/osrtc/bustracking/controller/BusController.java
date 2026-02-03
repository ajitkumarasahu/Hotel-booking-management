// Package declaration: controller package
package com.osrtc.bustracking.controller;

// Jackson imports (currently unused, may be used later for JSON parsing)
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// DAO and Model imports
import com.osrtc.bustracking.dao.BusDAO;
import com.osrtc.bustracking.model.Bus;
import com.osrtc.bustracking.model.FullBusData;

// Service layer imports
import com.osrtc.bustracking.service.AdminService;
import com.osrtc.bustracking.service.BusService;
import com.osrtc.bustracking.service.DriverService;
import com.osrtc.bustracking.service.LocationService;
import com.osrtc.bustracking.service.RouteService;

import javax.ws.rs.*;      // JAX-RS annotations: @GET, @POST, @PUT, @DELETE
import javax.ws.rs.core.*; // Response, MediaType, UriInfo, etc.
import java.util.List;     // To handle lists of buses
import java.util.logging.Logger; // Logging API calls

/**
 * BusController
 * -------------------------
 * REST Controller for handling Bus-related operations.
 * Base URL: /bus
 */
@Path("/bus")
@Consumes(MediaType.APPLICATION_JSON) // Request body: JSON
@Produces(MediaType.APPLICATION_JSON) // Response body: JSON
public class BusController {

    // Logger instance for logging API activity
    private static final Logger logger = Logger.getLogger(BusController.class.getName());

    // DAO for direct database operations
    BusDAO dao = new BusDAO();

    // Service layer for complex business logic
    private BusService busService = new BusService();

    /**
     * GET /bus
     * Fetch all buses
     */
    @GET
    public List<Bus> getAll() {
        logger.info("GET /bus - Fetch all buses");

        // Fetch all buses from DAO
        List<Bus> buses = dao.getAllBuses();

        // Log number of buses fetched
        logger.info("Total buses fetched: " + (buses != null ? buses.size() : 0));

        return buses;
    }

    /**
     * GET /bus/{id}
     * Fetch bus by ID
     */
    @GET
    @Path("/{id}")
    public Response getBusById(@PathParam("id") int id) {
        logger.info("GET /bus/" + id + " - Fetch bus by ID");

        Bus b = dao.getBusById(id);

        if (b == null) {
            logger.warning("Bus not found with ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Bus not found\"}")
                    .build();
        } else {
            logger.info("Bus found with ID: " + id);
            return Response.ok(b).build();
        }
    }

    /**
     * POST /bus
     * Add a new bus
     */
    @POST
    public Response add(Bus b) {
        logger.info("POST /bus - Add bus request received");

        if (dao.addBus(b)) {
            logger.info("Bus added successfully. BusId: " + b.getBusId());
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Bus added successfully\"}")
                    .build();
        } else {
            logger.warning("Bus addition failed");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Bus addition failed\"}")
                    .build();
        }
    }

    /**
     * POST /bus/multiplebus
     * Add multiple buses in bulk
     */
    @POST
    @Path("/multiplebus")
    public Response addMultipleBuses(List<Bus> buses) {
        logger.info("POST /bus/multiplebus - Bulk bus insert request");
        logger.info("Total buses received: " + (buses != null ? buses.size() : 0));

        boolean isAdded = dao.multipleBusInsert(buses);

        if (isAdded) {
            logger.info("Multiple buses added successfully");
            return Response.ok("{\"message\":\"Buses added successfully\"}").build();
        } else {
            logger.warning("Failed to add multiple buses");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Failed to add multiple buses\"}")
                    .build();
        }
    }

    /**
     * PUT /bus/{id}
     * Update bus by ID
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Bus b) {
        logger.info("PUT /bus/" + id + " - Update bus request");

        b.setBusId(id);

        if (dao.updateBus(b)) {
            logger.info("Bus updated successfully. ID: " + id);
            return Response.ok()
                    .entity("{\"message\":\"Bus updated successfully\"}")
                    .build();
        } else {
            logger.warning("Bus not found for update. ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Bus not found\"}")
                    .build();
        }
    }

    /**
     * PUT /bus/bulk
     * Update multiple buses at once
     */
    @PUT
    @Path("/bulk")
    public Response bulkUpdateBuses(List<Bus> buses) {
        logger.info("PUT /bus/bulk - Bulk update buses request");
        logger.info("Bulk update buses count: " + (buses != null ? buses.size() : 0));

        try {
            Object result = busService.bulkUpdateBuses(buses);
            return Response.ok(result)
                    .entity("{\"message\":\"Selected buses updated successfully\"}")
                    .build();
        } catch (Exception e) {
            logger.severe("Bulk update failed: " + e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * DELETE /bus/{id}
     * Delete bus by ID
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        logger.info("DELETE /bus/" + id + " - Delete bus request");

        if (dao.deleteBus(id)) {
            logger.info("Bus deleted successfully. ID: " + id);
            return Response.ok("{\"message\":\"Bus deleted successfully\"}").build();
        } else {
            logger.warning("Bus not found for deletion. ID: " + id);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"message\":\"Bus not found\"}")
                    .build();
        }
    }

    /**
     * DELETE /bus/bulk
     * Delete multiple buses by IDs
     */
    @DELETE
    @Path("/bulk")
    public Response bulkDeleteBuses(List<Integer> busIds) {
        logger.info("DELETE /bus/bulk - Bulk delete buses request");

        try {
            for (Integer id : busIds) {
                logger.info("Deleting bus ID: " + id);
                if (!dao.deleteBus(id)) {
                    logger.warning("Failed to delete bus ID: " + id);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Failed to delete bus with ID: " + id)
                            .build();
                }
            }
            logger.info("All buses deleted successfully");
            return Response.ok("{\"message\":\"All buses deleted successfully\"}").build();
        } catch (Exception e) {
            logger.severe("Bulk bus deletion failed: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * PUT /bus/assignDriver/{busId}/{driverId}
     * Assign a driver to a bus
     */
    @PUT
    @Path("/assignDriver/{busId}/{driverId}")
    public Response assignDriverToBus(@PathParam("busId") int busId,
                                      @PathParam("driverId") int driverId) {
        logger.info("PUT /bus/assignDriver - busId=" + busId + ", driverId=" + driverId);

        boolean isAssigned = dao.assignDriverToBus(busId, driverId);

        if (isAssigned) {
            logger.info("Driver assigned successfully. busId=" + busId + ", driverId=" + driverId);
            return Response.ok("{\"message\":\"Driver assigned to bus successfully\"}").build();
        } else {
            logger.warning("Failed to assign driver. busId=" + busId + ", driverId=" + driverId);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Failed to assign driver to bus\"}")
                    .build();
        }
    }

    /**
     * PUT /bus/updateStatus/{busId}/{status}
     * Update status of a bus
     */
    @PUT
    @Path("/updateStatus/{busId}/{status}")
    public Response updateBusStatus(@PathParam("busId") int busId,
                                    @PathParam("status") String status) {
        logger.info("PUT /bus/updateStatus - busId=" + busId + ", status=" + status);

        boolean isUpdated = dao.updateBusStatus(busId, status);

        if (isUpdated) {
            logger.info("Bus status updated successfully. busId=" + busId + ", status=" + status);
            return Response.ok("{\"message\":\"Bus status updated successfully\"}").build();
        } else {
            logger.warning("Failed to update bus status. busId=" + busId + ", status=" + status);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"Failed to update bus status\"}")
                    .build();
        }
    }

    /**
     * POST /bus/addfulldata
     * Add full bus data (Route + Driver + Bus + Location + Admin) in one request
     */
    @POST
    @Path("/addfulldata")
    public Response addFullBusData(FullBusData data) {
        logger.info("POST /bus/addfulldata - Add full bus data request");

        try {
            // Service instances for different entities
            RouteService routeService = new RouteService();
            DriverService driverService = new DriverService();
            BusService busService = new BusService();
            LocationService locationService = new LocationService();
            AdminService adminService = new AdminService();

            // Stepwise addition
            logger.info("STEP 1: Adding route");
            routeService.addRoute(data.getRoute());

            logger.info("STEP 2: Adding driver");
            driverService.addDriver(data.getDriver());

            logger.info("STEP 3: Adding bus");
            data.getBus().setRouteId(data.getRoute().getRouteId());
            data.getBus().setDriverId(data.getDriver().getDriverId());
            busService.addBus(data.getBus());

            logger.info("STEP 4: Adding location");
            data.getLocation().setBusId(data.getBus().getBusId());
            locationService.addLocation(data.getLocation());

            logger.info("STEP 5: Adding admin");
            adminService.addAdmin(data.getAdmin());

            logger.info("All entities added successfully");
            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\": \"All entities added successfully!\"}")
                    .build();
        } catch (Exception e) {
            logger.severe("Full bus data insert failed: " + e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}
