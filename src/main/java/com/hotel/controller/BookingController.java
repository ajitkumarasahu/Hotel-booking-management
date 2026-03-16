package com.hotel.controller;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/bookings/*")
public class BookingController extends HttpServlet {

    private BookingService bookingService=new BookingService();

    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException{

        BufferedReader br=req.getReader();
        StringBuilder json=new StringBuilder();
        String line;

        while((line=br.readLine())!=null){ json.append(line); }

        JSONObject obj=new JSONObject(json.toString());

        Booking booking=new Booking(
                obj.getLong("userId"),
                obj.getLong("roomId"),
                Date.valueOf(obj.getString("checkIn")),
                Date.valueOf(obj.getString("checkOut")),
                new BigDecimal(obj.getDouble("totalPrice")),
                obj.getString("status")
        );

        boolean result=bookingService.createBooking(booking);

        if(result){
            resp.setContentType("application/json");
            resp.setStatus(201);
            resp.getWriter().print("{\"message\":\"Room Booked successfully\"}");
        } else {
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"message\":\"Room Booked failed\"}");
        }
    }

    protected void doPut(HttpServletRequest req,HttpServletResponse resp)throws IOException{

        BufferedReader br=req.getReader();
        StringBuilder json=new StringBuilder();
        String line;

        while((line=br.readLine())!=null){ json.append(line); }

        JSONObject obj=new JSONObject(json.toString());

        Booking booking=new Booking();

        booking.setBookingId(obj.getLong("bookingId"));
        booking.setUserId(obj.getLong("userId"));
        booking.setRoomId(obj.getLong("roomId"));
        booking.setCheckIn(Date.valueOf(obj.getString("checkIn")));
        booking.setCheckOut(Date.valueOf(obj.getString("checkOut")));
        booking.setTotalPrice(new BigDecimal(obj.getDouble("totalPrice")));
        booking.setStatus(obj.getString("status"));

        boolean result=bookingService.updateBooking(booking);
        if(result){
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"message\":\"Room Upadated successfully\"}");
        }else{
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"message\":\"Room Upadated Failed\"}");
        }
    }

    protected void doDelete(HttpServletRequest req,HttpServletResponse resp)throws IOException{

        long id=Long.parseLong(req.getParameter("bookingId"));

        boolean result=bookingService.deleteBooking(id);

       if(result){
            resp.setContentType("application/json");
            resp.setStatus(200);
            resp.getWriter().print("{\"message\":\"Room Delete successfully\"}");
        }else{
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print("{\"message\":\"Room Delete Failed\"}");
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        String bookingId = req.getParameter("bookingId");
        String roomId = req.getParameter("roomId");

        // 1️⃣ GET SINGLE BOOKING
        if(bookingId != null){

            Booking b = bookingService.getBookingById(Long.parseLong(bookingId));

            JSONObject obj = new JSONObject();

            obj.put("bookingId", b.getBookingId());
            obj.put("userId", b.getUserId());
            obj.put("roomId", b.getRoomId());
            obj.put("checkIn", b.getCheckIn());
            obj.put("checkOut", b.getCheckOut());
            obj.put("totalPrice", b.getTotalPrice());
            obj.put("status", b.getStatus());

            resp.getWriter().print(obj);

            return;
        }

        // 2️⃣GET ALL BOOKINGS
        List<Booking> list = bookingService.getAllBookings();

        JSONArray arr = new JSONArray();

        for(Booking b:list){

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

        resp.getWriter().print(arr);
    }

    protected void doGetHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        long userId = Long.parseLong(req.getParameter("userId"));

        List<Booking> list = bookingService.getBookingHistory(userId);

        JSONArray arr = new JSONArray();

        for(Booking b:list){

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

        resp.getWriter().print(arr);
    }
}