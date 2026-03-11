package com.hotel.controller;

import com.hotel.model.Room;
import com.hotel.service.RoomService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.*;
//import java.math.BigDecimal;
import java.util.List;

import javax.annotation.processing.Generated;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/rooms/*")
@Produces(MediaType.APPLICATION_JSON)  
@Consumes(MediaType.APPLICATION_JSON) 
public class RoomController extends HttpServlet {

    private RoomService roomService = new RoomService();

    @Generated(value = "RoomController - GET /api/rooms")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        //Get parameters from URL
        String idParam = req.getParameter("roomId");
        String roomTypeParam = req.getParameter("roomtype");
        String priceParam = req.getParameter("price");
        String capacityParam = req.getParameter("capacity");
        String statusParam = req.getParameter("status");

        if(idParam != null || roomTypeParam != null || priceParam != null || capacityParam != null || statusParam != null){
            JSONObject json = new JSONObject();

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

            resp.getWriter().print(json);
            return;
        }

        List<Room> rooms = roomService.getAllRooms();

        JSONArray array = new JSONArray();

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

        resp.setContentType("application/json");
        resp.getWriter().print(array);
    }

    //Get room by ID
    @Generated(value = "RoomController - GET /api/rooms/{id}")
    protected void doGetById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Room ID is required");
            return;
        }

        long id = Long.parseLong(pathInfo.substring(1));

        Room room = roomService.getRoomById(id);

        if (room == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Room not found");
            return;
        }

        JSONObject obj = new JSONObject();

        obj.put("roomId", room.getRoomId());
        obj.put("hotelId", room.getHotelId());
        obj.put("roomType", room.getRoomType());
        obj.put("price", room.getPrice());
        obj.put("capacity", room.getCapacity());
        obj.put("status", room.getStatus());

        resp.setContentType("application/json");
        resp.getWriter().print(obj);

        if(room != null){
           resp.getWriter().print("{\"found\":Failed to find room }");
        } 
    }
    
    @Generated(value = "RoomController - POST /api/rooms")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        JSONObject obj = new JSONObject(json.toString());

        Room room = new Room(
                obj.getLong("hotelId"),
                obj.getString("roomType"),
                obj.getBigDecimal("price"),
                obj.getInt("capacity"),
                obj.getString("status")
        );

        boolean result = roomService.addRoom(room);

        if(result){
            resp.setStatus(201);
            resp.getWriter().print("{\"created\":Room added successfully }");
        } else {
            resp.getWriter().print("{\"created\":Failed to add room }");  
        }
    }

    @Generated(value = "RoomController - PUT /api/rooms")
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        BufferedReader reader = req.getReader();
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }

        JSONObject obj = new JSONObject(json.toString());

        Room room = new Room();

        room.setRoomId(obj.getLong("roomId"));
        room.setHotelId(obj.getLong("hotelId"));
        room.setRoomType(obj.getString("roomType"));
        room.setPrice(obj.getBigDecimal("price"));
        room.setCapacity(obj.getInt("capacity"));
        room.setStatus(obj.getString("status"));

        boolean result = roomService.updateRoom(room);

        if(result){
            resp.setStatus(200);
            resp.getWriter().print("{\"updated\":Room updated successfully }");
        } else {
            resp.getWriter().print("{\"updated\":Failed to update room }");  
        }
    }
    
    @Generated(value = "RoomController - DELETE /api/rooms")
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        long id = Long.parseLong(req.getParameter("roomId"));

        boolean result = roomService.deleteRoom(id);

        if(result){
            resp.setStatus(200);
            resp.getWriter().print("{\"deleted\":Room deleted successfully }");
        } else {
            resp.getWriter().print("{\"deleted\":Failed to delete room }");  
        }
    }
}