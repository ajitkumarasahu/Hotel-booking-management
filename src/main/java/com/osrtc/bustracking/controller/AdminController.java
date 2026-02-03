// Package declaration: defines controller package
package com.osrtc.bustracking.controller;

// DAO and Model imports
import com.osrtc.bustracking.dao.AdminDAO;
import com.osrtc.bustracking.model.Admin;

// JAX-RS imports for REST API handling
import javax.ws.rs.*;      // @GET, @POST, @PUT, @DELETE
import javax.ws.rs.core.*; // Response, MediaType, UriInfo, UriBuilder, etc.
import java.util.List;     // Used to handle lists of admins
import java.util.logging.Logger; // Logging API activities

/**
 * AdminController
 * -------------------------
 * REST Controller to manage Admin entity operations.
 * Base URL: /admin
 */
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON) // Responses in JSON
@Consumes(MediaType.APPLICATION_JSON) // Requests in JSON
public class AdminController {

    // Logger instance for debugging and monitoring
    private static final Logger logger = Logger.getLogger(AdminController.class.getName());

    // DAO for admin persistence
    private final AdminDAO dao = new AdminDAO();

    /**
     * GET /admin
     * Fetch all admins
     */
    @GET
    public List<Admin> getAll() {
        logger.info("GET /admin - Fetch all admins");

        List<Admin> admins = dao.getAllAdmins(); // Fetch admins from database

        logger.info("Total admins fetched: " + (admins != null ? admins.size() : 0));

        return admins;
    }

    /**
     * GET /admin/{id}
     * Fetch admin by ID
     */
    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        logger.info("GET /admin/" + id + " - Fetch admin by ID");

        Admin admin = dao.getAdminById(id);

        if (admin == null) {
            logger.warning("Admin not found. ID: " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        logger.info("Admin found. ID: " + id);
        return Response.ok(admin).build();
    }

    /**
     * POST /admin
     * Add a new admin
     */
    @POST
    public Response add(Admin admin, @Context UriInfo uriInfo) {
        logger.info("POST /admin - Add admin request received");

        boolean created = dao.addAdmin(admin);

        if (created) {
            logger.info("Admin added successfully. ID: " + admin.getAdminId());

            // Build URI for newly created admin
            UriBuilder builder = uriInfo.getAbsolutePathBuilder()
                                        .path(String.valueOf(admin.getAdminId()));

            // Return 201 CREATED with admin entity
            return Response.created(builder.build())
                           .entity(admin)
                           .build();
        }

        logger.warning("Failed to add admin");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * PUT /admin/{id}
     * Update admin by ID
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Admin admin) {
        logger.info("PUT /admin/" + id + " - Update admin request");

        admin.setAdminId(id);

        if (dao.updateAdmin(admin)) {
            logger.info("Admin updated successfully. ID: " + id);
            return Response.ok(admin).build();
        }

        logger.warning("Admin not found for update. ID: " + id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * DELETE /admin/{id}
     * Delete admin by ID
     */
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        logger.info("DELETE /admin/" + id + " - Delete admin request");

        if (dao.deleteAdmin(id)) {
            logger.info("Admin deleted successfully. ID: " + id);
            return Response.noContent().build(); // 204 No Content
        }

        logger.warning("Admin not found for deletion. ID: " + id);
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    /**
     * POST /admin/login
     * Admin login endpoint
     */
    @POST
    @Path("/login")
    public Response login(Credentials credentials) {
        logger.info("POST /admin/login - Login attempt for username: " + credentials.username);

        // Fetch admin by username
        Admin admin = dao.getAdminByUsername(credentials.username);

        // Validate password
        if (admin != null && admin.getPassword().equals(credentials.password)) {
            logger.info("Login successful for username: " + credentials.username);
            return Response.ok(admin).build();
        }

        logger.warning("Login failed for username: " + credentials.username);
        return Response.status(Response.Status.UNAUTHORIZED).build(); // 401 Unauthorized
    }

    /**
     * Inner class to receive login credentials
     */
    public static class Credentials {
        public String username;
        public String password;
    }
}
