// Package declaration - defines folder structure
package com.hotel.controller;

// Import User model class
import com.hotel.model.User;

// Import service class to handle business logic
import com.hotel.service.UserService;

// Import servlet annotations and classes
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

// JSON libraries for request/response handling
import org.json.JSONArray;
import org.json.JSONObject;

// Java utilities
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List; 

// Annotation (not really required here, just metadata)
import javax.annotation.processing.Generated;

// Map servlet to URL: /users/*
@WebServlet("/users/*")

// Response type is JSON
@Produces(MediaType.APPLICATION_JSON)  

// Request type expected is JSON
@Consumes(MediaType.APPLICATION_JSON) 

// Controller class extending HttpServlet
public class UserController extends HttpServlet {

    // Create service object to call business logic
    private UserService userService = new UserService();

    // ===============================
    // GET - Fetch User by ID 
    // ===============================
    @Generated(value = "UserController - GET /users/{id}")
    protected void GetbyId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Set response type JSON
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read query parameters from URL
        String userIdParam = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");

        // Create JSON object to store values
        JSONObject json = new JSONObject();

        // Add parameters to JSON if present
        if (userIdParam != null) {
            int userId = Integer.parseInt(userIdParam);
            json.put("userId", userId);
        }

        if (name != null) {
            json.put("name", name);
        }

        if (email != null) {
            json.put("email", email);
        }
        if (phone != null) {
            json.put("phone", phone);
        }

        if (role != null) {
            json.put("role", role);
        }

        // Print JSON (debug / extra response)
        out.print(json.toString());

        // Get path info (/users/{id})
        String pathInfo = request.getPathInfo();

        // Check if ID is present in URL
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                // Extract ID from URL
                int userId = Integer.parseInt(pathInfo.substring(1));

                // Call service to fetch user
                User user = userService.getUserById(userId);

                if (user != null) {
                    // Convert user to JSON
                    JSONObject obj = new JSONObject();
                    obj.put("userId", user.getUserId());
                    obj.put("name", user.getName());
                    obj.put("email", user.getEmail());
                    obj.put("phone", user.getPhone());
                    obj.put("role", user.getRole());

                    // Send response
                    out.print(obj.toString());
                } else {
                    // User not found
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"message\":\"User not found\"}");
                }
            } catch (NumberFormatException e) {
                // Invalid ID format
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"message\":\"Invalid user ID format\"}");
            }
        } else {
            // ID missing in URL
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"User ID required in URL\"}");
        }
    }

    // ===============================
    // GET - Fetch User by Email
    // ===============================
    @Generated(value = "UserController - GET /users?email={email}")
    protected void GetByEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read email parameter
        String email = request.getParameter("email");

        if (email != null && !email.isEmpty()) {

            // Fetch user by email
            User user = userService.getUserByEmail(email);

            if (user != null) {
                // Convert to JSON
                JSONObject obj = new JSONObject();
                obj.put("userId", user.getUserId());
                obj.put("name", user.getName());
                obj.put("email", user.getEmail());
                obj.put("phone", user.getPhone());
                obj.put("role", user.getRole());

                out.print(obj.toString());
            } else {
                // Not found
                response.setStatus(404);
                out.print("{\"message\":\"User not found\"}");
            }
        } else {
            // Email missing
            response.setStatus(400);
            out.print("{\"message\":\"Email parameter required\"}");
        }
    }

    // ===============================
    // GET - Fetch All Users
    // ===============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Check routing based on query params
        String userIdParam = request.getParameter("userId");
        String emailParam = request.getParameter("email");

        if (userIdParam != null) {
            GetbyId(request, response);
            return;
        } else if (emailParam != null) {
            GetByEmail(request, response);
            return;
        }

        // Read optional filters (not used effectively here)
        String userId = request.getParameter("userId");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String role = request.getParameter("role");
        
        JSONObject json = new JSONObject();

        // Add filters if present
        if (userId != null) json.put("userId", userId);
        if (name != null) json.put("name", name);
        if (email != null) json.put("email", email);
        if (phone != null) json.put("phone", phone);
        if (role != null) json.put("role", role);

        // Fetch all users
        List<User> users = userService.getAllUsers();

        JSONArray jsonArray = new JSONArray();

        // Convert list to JSON array
        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("userId", user.getUserId());
            obj.put("name", user.getName());
            obj.put("email", user.getEmail());
            obj.put("phone", user.getPhone());
            obj.put("role", user.getRole());
            jsonArray.put(obj);
        }

        // Send response
        out.print(jsonArray.toString());
    }

    // ===============================
    // PUT - Update User
    // ===============================
    @Generated(value = "UserController - PUT /users/{id}")
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read request body
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }

        // Convert body to JSON
        JSONObject json = new JSONObject(sb.toString());

        // Map JSON to User object
        User user = new User();
        user.setUserId(json.getInt("userId"));
        user.setName(json.getString("name"));
        user.setPhone(json.getString("phone"));
        user.setRole(json.getString("role"));

        // Update user
        boolean updated = userService.updateUser(user);

        if (updated) {
            response.setStatus(200);
            out.print("{\"message\":\"User updated successfully\"}");
        } else {
            response.setStatus(400);
            out.print("{\"message\":\"Update failed\"}");
        }
    }

    // ===============================
    // DELETE - Delete User
    // ===============================
    @Generated(value = "UserController - DELETE /users/{id}")
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read userId from query param
        String userIdParam = request.getParameter("userId");

        if (userIdParam == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"message\":\"User ID required\"}");
            return;
        }

        int userId = Integer.parseInt(userIdParam);

        // Delete user
        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            response.setStatus(200);
            out.print("{\"message\":\"User deleted successfully\"}");
        } else {
            response.setStatus(400);
            out.print("{\"message\":\"Delete failed\"}");
        }
    }
}