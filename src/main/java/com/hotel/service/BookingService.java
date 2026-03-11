package com.hotel.service;

import com.hotel.dao.BookingDAO;
import com.hotel.model.Booking;

import java.util.List;

public class BookingService {

    private BookingDAO bookingDAO=new BookingDAO();

    public List<Booking> getAllBookings(){
        return bookingDAO.getAllBookings();
    }

    public Booking getBookingById(long id){
        return bookingDAO.getBookingById(id);
    }

    public boolean createBooking(Booking booking){

        if(booking.getCheckOut().before(booking.getCheckIn())){
            throw new RuntimeException("Check-out must be after check-in");
        }

        return bookingDAO.createBooking(booking);
    }

    public boolean updateBooking(Booking booking){
        return bookingDAO.updateBooking(booking);
    }

    public boolean deleteBooking(long id){
        return bookingDAO.deleteBooking(id);
    }
}