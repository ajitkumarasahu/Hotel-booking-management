// Define package (organizes files in project)
package com.hotel.controller;

// Import Room model class
import com.hotel.model.Room;

// Import service layer (business logic)
import com.hotel.service.RoomService;

// Servlet annotations and HTTP classes
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

// REST-related annotations (not fully used here but for API style)
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.*; // For input/output handling
import java.util.List;

// Annotation (not required for logic, just metadata)
import javax.annotation.processing.Generated;

// JSON handling classes
import org.json.JSONArray;
import org.json.JSONObject;

// Map this servlet to URL: /rooms/*
@WebServlet("/rooms/*")

// Specify API produces JSON
@Produces(MediaType.APPLICATION_JSON)

// Specify API consumes JSON
@Consumes(MediaType.APPLICATION_JSON) 

// Controller class extending HttpServlet
public class RoomController extends HttpServlet {

    // Create service object to handle business logic
    private RoomService roomService = new RoomService();

    // ===================== GET ALL ROOMS =====================
    @Generated(value = "RoomController - GET /api/rooms")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Set response type to JSON
        resp.setContentType("application/json");

        // Get query parameters from URL
        String idParam = req.getParameter("roomId");
        String roomTypeParam = req.getParameter("roomtype");
        String priceParam = req.getParameter("price");
        String capacityParam = req.getParameter("capacity");
        String statusParam = req.getParameter("status");

        // If any parameter is present → return them as JSON (filter simulation)
        if(idParam != null || roomTypeParam != null || priceParam != null || capacityParam != null || statusParam != null){

            JSONObject json = new JSONObject();

            // Add only non-null parameters
            if(idParam != null){
                json.put("roomId", idParam);
            }
            if(roomTypeParam != null){
                json.put("roomType", roomTypeParam);
            }
            if(priceParam != null){
                json.put("price", priceParam);
            }
            if(capacityParam != null){
                json.put("capacity", capacityParam);
            }
            if(statusParam != null){
                json.put("status", statusParam);
            }

            // Send JSON response
            resp.getWriter().print(json);
            return;
        }

        // Fetch all rooms from service
        List<Room> rooms = roomService.getAllRooms();

        // Create JSON array
        JSONArray array = new JSONArray();

        // Loop through rooms and convert to JSON
        for (Room r : rooms) {

            JSONObject obj = new JSONObject();

            obj.put("roomId", r.getRoomId());
            obj.put("hotelId", r.getHotelId());
            obj.put("roomType", r.getRoomType());
            obj.put("price", r.getPrice());
            obj.put("capacity", r.getCapacity());
            obj.put("status", r.getStatus());

            array.put(obj);
        }

        // Send response
        resp.setStatus(200);
        resp.getWriter().print(array);
    }

    // ===================== GET ROOM BY ID =====================
    @Generated(value = "RoomController - GET /api/rooms/{id}")
    protected void doGetById(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        // Get path info (/5 → id = 5)
        String pathInfo = req.getPathInfo();

        // If no ID provided → error
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID is required");
            return;
        }

        // Extract ID
        long id = Long.parseLong(pathInfo.substring(1));

        // Get room from service
        Room room = roomService.getRoomById(id);

        // If not found → 404
        if (room == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Room not found");
            return;
        }

        // Convert room to JSON
        JSONObject obj = new JSONObject();

        obj.put("roomId", room.getRoomId());
        obj.put("hotelId", room.getHotelId());
        obj.put("roomType", room.getRoomType());
        obj.put("price", room.getPrice());
        obj.put("capacity", room.getCapacity());
        obj.put("status", room.getStatus());

        // Send response
        resp.getWriter().print(obj);

        // ❌ LOGIC ERROR: This block is wrong (room is already not null)
        if(room != null){
           resp.getWriter().print("{\"found\":Failed to find room }");
        } 
    }
    
    // ===================== CREATE ROOM =====================
    @Generated(value = "RoomController - POST /api/rooms")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Read JSON body from request
        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        // Convert string to JSON object
        JSONObject obj = new JSONObject(json.toString());

        // Create Room object using JSON data
        Room room = new Room(
                obj.getLong("hotelId"),
                obj.getString("roomType"),
                obj.getBigDecimal("price"),
                obj.getInt("capacity"),
                obj.getString("status")
        );

        // Call service to save room
        boolean result = roomService.addRoom(room);

        // Send response
        if(result){
            resp.setStatus(201); // Created
            resp.getWriter().print("{\"created\":Room added successfully }");
        } else {
            resp.setStatus(400);
            resp.getWriter().print("{\"created\":Failed to add room }");  
        }
    }

    // ===================== UPDATE ROOM =====================
    @Generated(value = "RoomController - PUT /api/rooms")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Read request body
        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        JSONObject obj = new JSONObject(json.toString());

        // Create empty Room object
        Room room = new Room();

        // Set values from JSON
        room.setRoomId(obj.getLong("roomId"));
        room.setHotelId(obj.getLong("hotelId"));
        room.setRoomType(obj.getString("roomType"));
        room.setPrice(obj.getBigDecimal("price"));
        room.setCapacity(obj.getInt("capacity"));
        room.setStatus(obj.getString("status"));

        // Update room
        boolean result = roomService.updateRoom(room);

        // Send response
        if(result){
            resp.setStatus(200);
            resp.getWriter().print("{\"updated\":Room updated successfully }");
        } else {
            resp.setStatus(400);
            resp.getWriter().print("{\"updated\":Failed to update room }");  
        }
    }
    
    // ===================== DELETE ROOM =====================
    @Generated(value = "RoomController - DELETE /api/rooms")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Get roomId from request parameter
        long id = Long.parseLong(req.getParameter("roomId"));

        // Delete room
        boolean result = roomService.deleteRoom(id);

        // Send response
        if(result){
            resp.setStatus(200);
            resp.getWriter().print("{\"deleted\":Room deleted successfully }");
        } else {
            resp.setStatus(400);
            resp.getWriter().print("{\"deleted\":Failed to delete room }");  
        }
    }
}