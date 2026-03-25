package com.hotel.filter; // Defines the package where this filter class is stored

import jakarta.servlet.*; // Imports core servlet classes like Filter, FilterChain, etc.
import jakarta.servlet.annotation.WebFilter; // Used to declare a filter with annotation
import jakarta.servlet.http.*; // Imports HTTP-specific servlet classes

import java.io.IOException; // Required for handling IO exceptions

@WebFilter("/api/*") // This filter will intercept all requests starting with /api/
public class RoleFilter implements Filter { // Declares a filter class

    // This method is called for every request that matches /api/*
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Cast generic request/response to HTTP-specific versions
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Get the requested URL path (e.g., /api/hotels, /api/bookings)
        String path = req.getRequestURI();

        // Get user role stored earlier (likely by authentication filter or interceptor)
        String role = (String) req.getAttribute("userRole");

        // ================= ADMIN ONLY APIs =================
        // If request path contains these endpoints, only ADMIN should access
        if(path.contains("/hotels") || path.contains("/rooms") || path.contains("/dashboard")){

            // If role is missing OR not ADMIN
            if(role == null || !role.equals("ADMIN")){
                // Set HTTP status 403 (Forbidden)
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);

                // Send response message
                res.getWriter().write("Access Denied: Admin Only");

                // Stop further processing (request will not reach controller)
                return;
            }
        }

        // ================= USER APIs =================
        // Booking APIs require at least a logged-in user
        if(path.contains("/bookings")){

            // If no role is found → user is not authenticated
            if(role == null){
                // Set HTTP status 401 (Unauthorized)
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                // Send response message
                res.getWriter().write("Unauthorized");

                // Stop further processing
                return;
            }
        }

        // If all checks pass → continue request processing (next filter or controller)
        chain.doFilter(request,response);
    }
}