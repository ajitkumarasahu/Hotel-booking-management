package com.hotel.service;

import com.hotel.dao.BookingDAO;
import com.hotel.dao.RoomDAO;
import com.hotel.model.Booking;
import com.hotel.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookingService {

    private BookingDAO bookingDAO=new BookingDAO();
    private RoomDAO roomDAO = new RoomDAO();

    public List<Booking> getAllBookings(){
        return bookingDAO.getAllBookings();
    }

    public Booking getBookingById(long id){
        return bookingDAO.getBookingById(id);
    }

    public boolean updateBooking(Booking booking){
        return bookingDAO.updateBooking(booking);
    }

    public boolean deleteBooking(long id){
        return bookingDAO.deleteBooking(id);
    }

     // CREATE BOOKING
    public boolean createBooking(Booking booking){

        LocalDate checkIn = booking.getCheckIn().toLocalDate();
        LocalDate checkOut = booking.getCheckOut().toLocalDate();

        if(checkOut.isBefore(checkIn)){
            throw new RuntimeException("Check-out must be after check-in");
        }

        // CALCULATE NUMBER OF DAYS
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);

        if(days == 0) days = 1;

        // GET ROOM PRICE
        double price = roomDAO.getRoomPrice(booking.getRoomId());

        double total = price * days;

        booking.setTotalPrice(new java.math.BigDecimal(total));

        booking.setStatus("CONFIRMED");

        return bookingDAO.createBooking(booking);
    }

    public List<Booking> getBookingHistory(long userId){

        if(userId <= 0){
            throw new RuntimeException("Invalid user ID");
        }

        return bookingDAO.getBookingsByUserId(userId);
    }

    public boolean cancelBooking(long bookingId){

        Booking booking = bookingDAO.getBookingById(bookingId);

        if(booking == null){
            throw new RuntimeException("Booking not found");
        }

        if("CANCELLED".equals(booking.getStatus())){
            throw new RuntimeException("Already cancelled");
        }

        return bookingDAO.cancelBooking(bookingId);
    }
}