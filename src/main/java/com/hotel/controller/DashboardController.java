package com.hotel.controller;

import com.hotel.model.DashboardStats;
import com.hotel.service.DashboardService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private DashboardService service = new DashboardService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        DashboardStats stats = service.getStats();

        JSONObject obj = new JSONObject();

        obj.put("totalUsers",stats.getTotalUsers());
        obj.put("totalHotels",stats.getTotalHotels());
        obj.put("totalRooms",stats.getTotalRooms());
        obj.put("totalBookings",stats.getTotalBookings());
        obj.put("totalRevenue",stats.getTotalRevenue());

        resp.setContentType("application/json");
        resp.getWriter().print(obj);
    }
}