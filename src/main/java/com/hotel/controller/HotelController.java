package com.hotel.controller;

import com.hotel.model.Hotel;
import com.hotel.model.Room;
import com.hotel.service.HotelService;
import com.hotel.service.RoomService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.servlet.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.processing.Generated;

@WebServlet("/hotels/*")
@Produces(MediaType.APPLICATION_JSON)  
@Consumes(MediaType.APPLICATION_JSON) 
public class HotelController extends HttpServlet {

    private HotelService hotelService = new HotelService();

    // GET ALL HOTELS
    @Generated(value = "UserController - GET /hotels")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String role = (String) request.getAttribute("userRole");

        // 🔐 ROLE CHECK
        if(!"ADMIN".equals(role)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Access Denied: Admin Only");
            return;
        }
           
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Read query parameters (if any)
        String hotelIdParam = request.getParameter("hotelId");
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        JSONObject json = new JSONObject();

        if (hotelIdParam != null) {
            int hotelId = Integer.parseInt(hotelIdParam);
            json.put("hotelId", hotelId);
        }

        if (name != null) {
            json.put("name", name);
        }

        if (location != null) {
            json.put("location", location);
        }

        if (description != null) {
            json.put("description", description);
        }

        List<Hotel> hotels = hotelService.getAllHotels();

        JSONArray arr = new JSONArray();

        for(Hotel h : hotels){

            JSONObject obj = new JSONObject();
            obj.put("hotelId", h.getHotelId());
            obj.put("name", h.getName());
            obj.put("location", h.getLocation());
            obj.put("description", h.getDescription());

            arr.put(obj);
        }

        out.print(arr.toString());
    }

    // GET HOTEL BY ID
    @Generated(value = "UserController - GET /hotels/{id}")
    protected void GetbyId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String role = (String) request.getAttribute("userRole");

        // 🔐 ROLE CHECK
        if(!"ADMIN".equals(role)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Access Denied: Admin Only");
            return;
        }
           
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Extract hotelId from query parameter
        String hotelIdParam = request.getParameter("hotelId");
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        JSONObject json = new JSONObject();
        
        if(hotelIdParam != null){
            int hotelId = Integer.parseInt(hotelIdParam);
            json.put("hotelId", hotelId);
        }
        if(name != null){
            json.put("name", name);
        }
        if(location != null){
            json.put("location", location);
        }
        if(description != null){
            json.put("description", description);
        }

        int hotelId = Integer.parseInt(request.getParameter("hotelId"));

        Hotel hotel = hotelService.getHotelById(hotelId);

        if(hotel != null){
            JSONObject obj = new JSONObject();
            obj.put("hotelId", hotel.getHotelId());
            obj.put("name", hotel.getName());
            obj.put("location", hotel.getLocation());
            obj.put("description", hotel.getDescription());

            out.print(obj.toString());
        }else{
            response.setStatus(404);
            System.out.print("{\"message\":\"Hotel not found\"}");
        }
    }

    // CREATE HOTEL
    @Generated(value = "UserController - POST /hotels")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        String role = (String) request.getAttribute("userRole");

        // 🔐 ROLE CHECK
        if(!"ADMIN".equals(role)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Access Denied: Admin Only");
            return;
        }
            
        StringBuilder sb = new StringBuilder();
        String line;

        while((line=request.getReader().readLine())!=null){
            sb.append(line);
        }

        JSONObject json = new JSONObject(sb.toString());

        Hotel hotel = new Hotel(
                json.getString("name"),
                json.getString("location"),
                json.getString("description")
        );

        boolean created = hotelService.createHotel(hotel);

        PrintWriter out = response.getWriter();

        if(created){
            response.setContentType("application/json");
            response.setStatus(201);
            out.print("{\"message\":\"Hotel created successfully\"}");
        }else{
            response.setContentType("application/json");
            response.setStatus(400);
            out.print("{\"message\":\"Hotel Creation failed\"}");
        }
    }

    // UPDATE HOTEL
    @Generated(value = "UserController - PUT /hotels/{id}")
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String role = (String) request.getAttribute("userRole");

        // 🔐 ROLE CHECK
        if(!"ADMIN".equals(role)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Access Denied: Admin Only");
            return;
        }
           
        StringBuilder sb = new StringBuilder();
        String line;

        while((line=request.getReader().readLine())!=null){
            sb.append(line);
        }

        JSONObject json = new JSONObject(sb.toString());

        Hotel hotel = new Hotel();
        hotel.setHotelId(json.getInt("hotelId"));
        hotel.setName(json.getString("name"));
        hotel.setLocation(json.getString("location"));
        hotel.setDescription(json.getString("description"));

        boolean updated = hotelService.updateHotel(hotel);

        PrintWriter out = response.getWriter();

        if(updated){
            response.setContentType("application/json");
            response.setStatus(200);
            out.print("{\"message\":\"Hotel updated Successfully\"}");
        }else{
            response.setContentType("application/json");
            response.setStatus(400);
            out.print("{\"message\":\"Hotel Update failed\"}");
        }
    }

    // DELETE HOTEL
    @Generated(value = "UserController - DELETE /hotels/{id}")
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String role = (String) request.getAttribute("userRole");

        // 🔐 ROLE CHECK
        if(!"ADMIN".equals(role)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Access Denied: Admin Only");
            return;
        }
           
        int id = Integer.parseInt(request.getParameter("hotelId"));

        boolean deleted = hotelService.deleteHotel(id);

        PrintWriter out = response.getWriter();

        if(deleted){
            response.setContentType("application/json");
            response.setStatus(200);
            out.print("{\"message\":\"Hotel deleted Successfully\"}");
        }else{
            response.setContentType("application/json");
            response.setStatus(400);
            out.print("{\"message\":\"Hotel Delete failed\"}");
        }
    }

    protected void doGetRoomsByHotel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String role = (String) request.getAttribute("userRole");

        // 🔐 ROLE CHECK
        if(!"ADMIN".equals(role)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().print("Access Denied: Admin Only");
            return;
        }
           
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String hotelIdParam = request.getParameter("hotelId");

            if(hotelIdParam != null){

                long hotelId = Long.parseLong(hotelIdParam);

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
                return;
            }
    }

    protected void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String search = request.getParameter("search");
        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");

        if(search != null){

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

                out.print(arr.toString());
        }else{
            response.setStatus(400);
            out.print("{\"message\":\"Search parameter is required\"}");
        }
    }
}