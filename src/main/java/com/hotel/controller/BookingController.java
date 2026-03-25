// Define package
package com.hotel.controller;

// Import required classes
import com.hotel.model.Booking;          // Booking model (data object)
import com.hotel.service.BookingService; // Service layer (business logic)

import jakarta.servlet.annotation.WebServlet; // To define servlet URL mapping
import jakarta.servlet.http.*;                // HTTP request/response handling

import java.io.*;            // For input/output (BufferedReader, PrintWriter)
import java.math.BigDecimal; // For handling price values accurately
import java.sql.Date;        // For SQL Date format
import java.util.List;       // For list handling

import org.json.JSONArray;   // JSON Array handling
import org.json.JSONObject;  // JSON Object handling

// Map this servlet to URL: /bookings/*
@WebServlet("/bookings/*")
public class BookingController extends HttpServlet {

    // Create service object to handle business logic
    private BookingService bookingService = new BookingService();

    // ====================== CREATE BOOKING ======================
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Read JSON request body
        BufferedReader br = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;

        // Convert request body into String
        while ((line = br.readLine()) != null) { 
            json.append(line); 
        }

        // Convert String into JSON object
        JSONObject obj = new JSONObject(json.toString());

        // Create Booking object using JSON values
        Booking booking = new Booking(
                obj.getLong("userId"),                         // user id
                obj.getLong("roomId"),                         // room id
                Date.valueOf(obj.getString("checkIn")),        // check-in date
                Date.valueOf(obj.getString("checkOut")),       // check-out date
                new BigDecimal(obj.getDouble("totalPrice")),   // total price
                obj.getString("status")                        // booking status
        );

        // Call service to create booking
        boolean result = bookingService.createBooking(booking);

        // Send response based on result
        if (result) {
            resp.setContentType("application/json");
            resp.setStatus(201); // Created
            resp.getWriter().print("{\"message\":\"Room Booked successfully\"}");
        } else {
            resp.setContentType("application/json");
            resp.setStatus(400); // Bad request
            resp.getWriter().print("{\"message\":\"Room Booked failed\"}");
        }
    }

    // ====================== UPDATE BOOKING ======================
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Read JSON request
        BufferedReader br = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) { 
            json.append(line); 
        }

        JSONObject obj = new JSONObject(json.toString());

        // Create empty booking object
        Booking booking = new Booking();

        // Set updated values
        booking.setBookingId(obj.getLong("bookingId"));
        booking.setUserId(obj.getLong("userId"));
        booking.setRoomId(obj.getLong("roomId"));
        booking.setCheckIn(Date.valueOf(obj.getString("checkIn")));
        booking.setCheckOut(Date.valueOf(obj.getString("checkOut")));
        booking.setTotalPrice(new BigDecimal(obj.getDouble("totalPrice")));
        booking.setStatus(obj.getString("status"));

        // Call service to update booking
        boolean result = bookingService.updateBooking(booking);

        // Send response
        if (result) {
            resp.setContentType("application/json");
            resp.setStatus(200); // OK
            resp.getWriter().print("{\"message\":\"Room Updated successfully\"}");
        } else {
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"message\":\"Room Updated Failed\"}");
        }
    }

    // ====================== DELETE BOOKING ======================
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Get bookingId from request parameter
        long id = Long.parseLong(req.getParameter("bookingId"));

        // Call service to delete booking
        boolean result = bookingService.deleteBooking(id);

        // Send response
        if (result) {
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"message\":\"Room Delete successfully\"}");
        } else {
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"message\":\"Room Delete Failed\"}");
        }
    }

    // ====================== GET BOOKINGS ======================
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        // Get query parameters
        String bookingId = req.getParameter("bookingId");
        String roomId = req.getParameter("roomId");

        // 1️⃣ GET SINGLE BOOKING
        if (bookingId != null) {

            // Fetch booking by ID
            Booking b = bookingService.getBookingById(Long.parseLong(bookingId));

            // Convert to JSON
            JSONObject obj = new JSONObject();
            obj.put("bookingId", b.getBookingId());
            obj.put("userId", b.getUserId());
            obj.put("roomId", b.getRoomId());
            obj.put("checkIn", b.getCheckIn());
            obj.put("checkOut", b.getCheckOut());
            obj.put("totalPrice", b.getTotalPrice());
            obj.put("status", b.getStatus());

            // Send response
            resp.getWriter().print(obj);
            return;
        }

        // 2️⃣ GET ALL BOOKINGS
        List<Booking> list = bookingService.getAllBookings();

        JSONArray arr = new JSONArray();

        // Convert list to JSON array
        for (Booking b : list) {

            JSONObject obj = new JSONObject();

            obj.put("bookingId", b.getBookingId());
            obj.put("userId", b.getUserId());
            obj.put("roomId", b.getRoomId());
            obj.put("checkIn", b.getCheckIn());
            obj.put("checkOut", b.getCheckOut());
            obj.put("totalPrice", b.getTotalPrice());
            obj.put("status", b.getStatus());

            arr.put(obj);
        }

        // Send response
        resp.getWriter().print(arr);
    }

    // ====================== GET BOOKING HISTORY ======================
    protected void doGetHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        // Get userId
        long userId = Long.parseLong(req.getParameter("userId"));

        // Fetch booking history
        List<Booking> list = bookingService.getBookingHistory(userId);

        JSONArray arr = new JSONArray();

        // Convert to JSON array
        for (Booking b : list) {

            JSONObject obj = new JSONObject();

            obj.put("bookingId", b.getBookingId());
            obj.put("userId", b.getUserId());
            obj.put("roomId", b.getRoomId());
            obj.put("checkIn", b.getCheckIn());
            obj.put("checkOut", b.getCheckOut());
            obj.put("totalPrice", b.getTotalPrice());
            obj.put("status", b.getStatus());

            arr.put(obj);
        }

        // Send response
        resp.getWriter().print(arr);
    }

    // ====================== CANCEL BOOKING ======================
    protected void doCancel(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Get bookingId
        String bookingId = req.getParameter("bookingId");

        if (bookingId != null) {

            // Call service to cancel booking
            boolean result = bookingService.cancelBooking(Long.parseLong(bookingId));

            PrintWriter out = resp.getWriter();

            // Send response
            if (result) {
                resp.setContentType("application/json");
                resp.setStatus(200);
                out.print("{\"message\":\"Booking Cancelled successfully\"}");
            } else {
                resp.setContentType("application/json");
                resp.setStatus(400);
                out.print("{\"message\":\"Booking Cancel Failed\"}");
            }
        }
    }
}