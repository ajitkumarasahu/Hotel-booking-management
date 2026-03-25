package com.hotel.controller; // Defines the package where this controller class belongs

import com.hotel.model.DashboardStats; // Imports the DashboardStats model class (holds dashboard data)
import com.hotel.service.DashboardService; // Imports service class that fetches dashboard data

import jakarta.servlet.annotation.WebServlet; // Used to map servlet to a URL
import jakarta.servlet.http.*; // Imports HTTP servlet classes

import org.json.JSONObject; // Used to create JSON response

import java.io.IOException; // Handles input/output exceptions

// Maps this servlet to the URL "/dashboard"
@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    // Creates an instance of DashboardService to fetch data
    private DashboardService service = new DashboardService();

    // Handles HTTP GET requests
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Calls service method to get dashboard statistics
        DashboardStats stats = service.getStats();

        // Creates a JSON object to store response data
        JSONObject obj = new JSONObject();

        // Adds data to JSON object using values from stats
        obj.put("totalUsers", stats.getTotalUsers());     // Total users count
        obj.put("totalHotels", stats.getTotalHotels());   // Total hotels count
        obj.put("totalRooms", stats.getTotalRooms());     // Total rooms count
        obj.put("totalBookings", stats.getTotalBookings()); // Total bookings count
        obj.put("totalRevenue", stats.getTotalRevenue()); // Total revenue amount

        // Sets response content type to JSON
        resp.setContentType("application/json");

        // Sends JSON response to client (browser or frontend)
        resp.getWriter().print(obj);
    }
}