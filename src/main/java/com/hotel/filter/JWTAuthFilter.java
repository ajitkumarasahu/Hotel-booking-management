package com.hotel.filter; // Defines the package where this filter class belongs

import com.hotel.util.JWTUtil; // Imports utility class used for JWT operations (validation, extracting role)

import jakarta.servlet.*; // Provides servlet interfaces like Filter, FilterChain, etc.
import jakarta.servlet.annotation.WebFilter; // Used to declare a filter with annotation
import jakarta.servlet.http.HttpServletRequest; // For handling HTTP request data
import jakarta.servlet.http.HttpServletResponse; // For handling HTTP response data

import java.io.IOException; // Handles input-output exceptions

@WebFilter("/api/*") // This filter will be applied to all URLs starting with /api/
public class JWTAuthFilter implements Filter { // Defines a filter class implementing Filter interface

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast generic request/response to HTTP-specific ones
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI(); // Get the requested URL path

        // Allow authentication endpoints (like login/register) without JWT check
        if (path.contains("/auth/")) {
            chain.doFilter(request, response); // Pass request to next filter or resource
            return; // Stop further execution in this filter
        }

        String header = req.getHeader("Authorization"); // Get Authorization header from request

        // Check if header is missing OR doesn't start with "Bearer "
        if (header == null || !header.startsWith("Bearer ")) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP status 401
            res.getWriter().write("Missing Token"); // Send error message
            return; // Stop processing
        }

        String token = header.substring(7); // Extract token by removing "Bearer " prefix

        String role = JWTUtil.getRole(token); // Extract user role from JWT token

        // Store role in request so it can be used later (e.g., in controllers)
        req.setAttribute("userRole", role);

        // Validate token (returns null if invalid/expired)
        if (JWTUtil.validateToken(token) == null) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set HTTP status 401
            res.getWriter().write("Invalid Token"); // Send error message
            return; // Stop processing
        }

        // If everything is valid, continue request processing
        chain.doFilter(request, response);
    }
}