package com.hotel.controller; // Package declaration (organizes the class)

// Import model classes
import com.hotel.model.Hotel;
import com.hotel.model.Room;

// Import service layer classes (business logic)
import com.hotel.service.HotelService;
import com.hotel.service.RoomService;

// Servlet & REST-related imports
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.servlet.*;

// JSON handling
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
// Java utilities
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Generated;

// Maps this servlet to URL: /hotels/*
@WebServlet("/hotels/*")

// Specifies response and request format as JSON
@Produces(MediaType.APPLICATION_JSON)  
@Consumes(MediaType.APPLICATION_JSON) 

// Controller class extending HttpServlet
public class HotelController extends HttpServlet {

    // Service object to interact with business logic
    private HotelService hotelService = new HotelService();

    // ====================== GET ALL HOTELS ======================
    @Generated(value = "UserController - GET /hotels")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Get user role from request
        String role = (String) request.getAttribute("userRole");

        // 🔐 Allow only ADMIN users
        if(!"ADMIN".equals(role)){
            response.setStatus(203);
            response.setContentType("application/json");    
            response.getWriter().print("{\"message\":\"Access Denied: Admin Only\"}");
            return;
        }

        // Set response type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read optional query parameters
        String hotelIdParam = request.getParameter("hotelId");
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        // Create JSON object for filters (not used further here)
        JSONObject json = new JSONObject();

        if (hotelIdParam != null) json.put("hotelId", Integer.parseInt(hotelIdParam));
        if (name != null) json.put("name", name);
        if (location != null) json.put("location", location);
        if (description != null) json.put("description", description);

        // Fetch all hotels from service
        List<Hotel> hotels = hotelService.getAllHotels();

        // Convert list to JSON array
        JSONArray arr = new JSONArray();

        for(Hotel h : hotels){
            JSONObject obj = new JSONObject();
            obj.put("hotelId", h.getHotelId());
            obj.put("name", h.getName());
            obj.put("location", h.getLocation());
            obj.put("description", h.getDescription());

            arr.put(obj);
        }

        // Send response
        out.print(arr.toString());
    }

    // ====================== GET HOTEL BY ID ======================
    @Generated(value = "UserController - GET /hotels/{id}")
    protected void GetbyId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Role check
        String role = (String) request.getAttribute("userRole");
        if(!"ADMIN".equals(role)){
            response.setStatus(203);
            response.getWriter().print("{\"message\":\"Access Denied: Admin Only\"}");
            return;
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Get hotelId from query parameter
        String hotelIdParam = request.getParameter("hotelId");
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        // Create JSON object for filters 
        JSONObject json = new JSONObject();

        //Add parameters to JSON object if they exist
        if(hotelIdParam != null) {
            int hotelId = Integer.parseInt(hotelIdParam);
            json.put("hotelId", hotelId);
        }
        if(name != null) {
            json.put("name", name);
        }
        if(location != null) {
            json.put("location", location);
        }
        if(description != null) {
            json.put("description", description);
        }

        //print JSON object for debugging
        out.print(json.toString());

        //Get path info to extract hotelId
        String pathInfo = request.getPathInfo(); // e.g. /123

        // Check if pathInfo is valid and extract hotelId
        if(pathInfo != null && pathInfo.length() > 1){
            try {
                // Extract hotelId from path
                int hotelId = Integer.parseInt(pathInfo.substring(1));
                
                //call service to get hotel by ID
                Hotel hotel = hotelService.getHotelById(hotelId);

                if(hotel != null){
                    // Convert hotel details to JSON
                    JSONObject obj = new JSONObject();
                    obj.put("hotelId", hotel.getHotelId());
                    obj.put("name", hotel.getName());
                    obj.put("location", hotel.getLocation());
                    obj.put("description", hotel.getDescription());
                    
                    // Send hotel details as response
                    out.print(obj.toString());
                } else {
                    // If hotel not found, send 404 response
                    response.setStatus(404);
                    out.print("{\"message\":\"Hotel not found\"}");
                }
            } catch(NumberFormatException e){
                // If hotelId is not a valid integer, send 400 response
                response.setStatus(400);
                out.print("{\"message\":\"Invalid hotel ID format\"}");
            }
        } else {
            // If hotelId is missing in the URL, send 400 response
            response.setStatus(400);
            out.print("{\"message\":\"Hotel ID is required in the URL\"}");
        }

    }

    // ====================== CREATE HOTEL ======================
    @Generated(value = "UserController - POST /hotels")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        // Role check
            String role = (String) request.getAttribute("userRole");
            if(!"ADMIN".equals(role)){
                response.setStatus(203);
                response.getWriter().print("{\"message\":\"Access Denied: Admin Only\"}");
                return;
            }

            // Read request body
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;

            while((line=reader.readLine())!=null){
                json.append(line);
            }

            // Convert body to JSON
            // JSONObject json = new JSONObject(json.toString());

            // 🔥 Detect ARRAY (bulk)
            if(json.toString().startsWith("[")){

                JSONArray arr = new JSONArray(json.toString());

                List<Hotel> hotels = new ArrayList<>();

                for(int i=0;i<arr.length();i++){

                    JSONObject obj = arr.getJSONObject(i);

                    Hotel h = new Hotel();

                    h.setName(obj.getString("name"));
                    h.setLocation(obj.getString("location"));
                    h.setDescription(obj.getString("description"));

                    hotels.add(h);
                }

                boolean result = hotelService.bulkAddHotels(hotels);
                PrintWriter out = response.getWriter();

            if(result){
                response.setStatus(201);
                out.print("{\"message\":\"Hotel created successfully\"}");
            }else{
                response.setStatus(400);
                out.print("{\"message\":\"Hotel Creation failed\"}");
            }
        }else{
            // Convert body to JSON
            JSONObject obj = new JSONObject(json.toString());

            // Create hotel object
            Hotel hotel = new Hotel(
                obj.getString("name"),
                obj.getString("location"),
                obj.getString("description")
            );

            //call service to add hotel
            boolean result = hotelService.createHotel(hotel);
            
            //create PrintWriter to send response
            PrintWriter out = response.getWriter();

            if(result){
                response.setStatus(201);
                out.print("{\"message\":\"Hotel created successfully\"}");
            }else{
                response.setStatus(400);
                out.print("{\"message\":\"Hotel Creation failed\"}");
            }
        }
    }
    // ====================== UPDATE HOTEL ======================
    @Generated(value = "UserController - PUT /hotels/{id}")
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Role check
        String role = (String) request.getAttribute("userRole");
        if(!"ADMIN".equals(role)){
            response.setStatus(203);
            response.getWriter().print("{\"message\":\"Access Denied: Admin Only\"}");
            return;
        }

        // Read request body
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=request.getReader().readLine())!=null){
            sb.append(line);
        }

        JSONObject json = new JSONObject(sb.toString());

        // Set updated data
        Hotel hotel = new Hotel();
        hotel.setHotelId(json.getInt("hotelId"));
        hotel.setName(json.getString("name"));
        hotel.setLocation(json.getString("location"));
        hotel.setDescription(json.getString("description"));

        // Update in DB
        boolean updated = hotelService.updateHotel(hotel);

        PrintWriter out = response.getWriter();

        if(updated){
            response.setStatus(200);
            out.print("{\"message\":\"Hotel updated Successfully\"}");
        }else{
            response.setStatus(400);
            out.print("{\"message\":\"Hotel Update failed\"}");
        }
    }

    // ====================== DELETE HOTEL ======================
    @Generated(value = "UserController - DELETE /hotels/{id}")
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Role check
        String role = (String) request.getAttribute("userRole");
        if(!"ADMIN".equals(role)){
            response.setStatus(203);
            response.getWriter().print("{\"message\":\"Access Denied: Admin Only\"}");
            return;
        }

        // Get hotelId
        int id = Integer.parseInt(request.getParameter("hotelId"));

        // Delete hotel
        boolean deleted = hotelService.deleteHotel(id);

        PrintWriter out = response.getWriter();

        if(deleted){
            response.setStatus(200);
            out.print("{\"message\":\"Hotel deleted Successfully\"}");
        }else{
            response.setStatus(400);
            out.print("{\"message\":\"Hotel Delete failed\"}");
        }
    }

    // ====================== GET ROOMS BY HOTEL ======================
    protected void doGetRoomsByHotel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Role check
        String role = (String) request.getAttribute("userRole");
        if(!"ADMIN".equals(role)){
            response.setStatus(203);
            response.getWriter().print("{\"message\":\"Access Denied: Admin Only\"}");
            return;
        }

        response.setContentType("application/json");

        String hotelIdParam = request.getParameter("hotelId");

        if(hotelIdParam != null){
            long hotelId = Long.parseLong(hotelIdParam);

            // Fetch rooms
            List<Room> rooms = new RoomService().getRoomsByHotel(hotelId);

            JSONArray roomArray = new JSONArray();

            for(Room r : rooms){
                JSONObject obj = new JSONObject();
                obj.put("roomId",r.getRoomId());
                obj.put("roomType",r.getRoomType());
                obj.put("price",r.getPrice());
                obj.put("capacity",r.getCapacity());
                obj.put("status",r.getStatus());

                roomArray.put(obj);
            }

            response.getWriter().print(roomArray);
        }
    }

    // ====================== SEARCH HOTELS ======================
    protected void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        String search = request.getParameter("search");
        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");

        if(search != null){

            // Pagination logic
            int page = pageParam != null ? Integer.parseInt(pageParam) : 1;
            int size = sizeParam != null ? Integer.parseInt(sizeParam) : 5;

            List<Hotel> hotels = hotelService.searchHotels(search,page,size);

            JSONArray arr = new JSONArray();

            for(Hotel h : hotels){
                JSONObject obj = new JSONObject();
                obj.put("hotelId", h.getHotelId());
                obj.put("name", h.getName());
                obj.put("location", h.getLocation());
                obj.put("description", h.getDescription());

                arr.put(obj);
            }

            response.getWriter().print(arr.toString());

        } else {
            response.setStatus(400);
            response.getWriter().print("{\"message\":\"Search parameter is required\"}");
        }
    }
}