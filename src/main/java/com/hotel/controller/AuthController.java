// Package declaration (organizes this class inside controller layer)
package com.hotel.controller;

// Importing model class representing User entity
import com.hotel.model.User;

// Importing service class that handles business logic for users
import com.hotel.service.UserService;

// Utility class for generating JWT tokens (authentication)
import com.hotel.util.JWTUtil;

// Servlet and HTTP-related imports
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

// JSON library to parse request body
import org.json.JSONObject;

// Mapping this servlet to handle URLs starting with /auth/*
@WebServlet("/auth/*")
public class AuthController extends HttpServlet {

    // Creating instance of UserService to interact with business logic
    private UserService userService = new UserService();

    // Handles POST requests (for register and login)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get the path after /auth (e.g., /register or /login)
        String path = request.getPathInfo();

        // Set response type as JSON
        response.setContentType("application/json");

        // Writer to send response back to client
        PrintWriter out = response.getWriter();

        // ---------------- REGISTER API ----------------
        if ("/register".equals(path)) {

            // Read incoming JSON request body
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;

            // Convert request body into string
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse string into JSON object
            JSONObject json = new JSONObject(sb.toString());

            // Create User object using JSON data
            User user = new User(
                    json.getString("name"),
                    json.getString("email"),
                    json.getString("password"),
                    json.getString("phone"),
                    json.getString("role")
            );

            // Call service method to register user
            boolean success = userService.registerUser(user);

            if (success) {
                response.setContentType("application/json");
                response.setStatus(201); // HTTP 201 = Created
                out.print("{\"message\":\"Registered successfully\"}");
            } else {
                response.setContentType("application/json");
                response.setStatus(400); // HTTP 400 = Bad Request
                out.print("{\"message\":\"Registration failed\"}");
            }
        }

        // ---------------- LOGIN API ----------------
        if ("/login".equals(path)) {

            // Read incoming JSON request body
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;

            // Convert request body into string
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse string into JSON object
            JSONObject json = new JSONObject(sb.toString());

            // Authenticate user using email and password
            User user = userService.login(
                    json.getString("email"),
                    json.getString("password")
            );

            if (user != null) {
                // Generate JWT token for authenticated user
                String token = JWTUtil.generateToken(user.getEmail(), user.getRole());

                response.setStatus(200); // HTTP 200 = OK

                // Send token in response
                out.print("{\"token\":\"" + token + "\"}");

                // Send login success message (NOTE: this creates invalid JSON because of two prints)
                out.print("{\"message\":\"login successfully\"}");
            } else {
                response.setStatus(401); // HTTP 401 = Unauthorized
                response.setContentType("application/json");
                out.print("{\"message\":\"Invalid credentials\"}");
            }
        }
    }
}