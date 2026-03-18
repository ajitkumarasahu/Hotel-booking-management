package com.hotel.service;

import com.hotel.dao.HotelDAO;
import com.hotel.model.Hotel;

import java.util.List;

public class HotelService {

    private HotelDAO hotelDAO = new HotelDAO();

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = hotelDAO.getAllHotels();

        if (hotels == null || hotels.isEmpty()) {
            System.out.println("{\"message\":\"No Hotels found\"}");
        }

        return hotels;
    }

    public Hotel getHotelById(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Hotel ID must be greater than 0.");
        }

        Hotel hotel = hotelDAO.getHotelById(id);

        if (hotel == null) {
            throw new RuntimeException("Hotel not found with ID: " + id);
        }

        return hotel;
    }

    public boolean createHotel(Hotel hotel){

        if(hotel == null) {
            throw new IllegalArgumentException("Hotel object cannot be null.");
        }

        if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel name is required.");
        }

        if (hotel.getLocation() == null || hotel.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel location is required.");
        }

        if (hotel.getDescription() == null || hotel.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel description is required.");
        }

        return hotelDAO.addHotel(hotel);
    }

    public boolean updateHotel(Hotel hotel){
        
        if(hotel == null) {
            throw new IllegalArgumentException("Hotel object cannot be null.");
        }

        if (hotel.getHotelId() <= 0) {
            throw new IllegalArgumentException("Hotel ID must be greater than 0.");
        }

        Hotel existingHotel = hotelDAO.getHotelById(hotel.getHotelId());

        if (existingHotel == null) {
            throw new RuntimeException("Hotel not found with ID: " + hotel.getHotelId());
        }

        if (hotel.getName() == null || hotel.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel name is required.");
        }

        if (hotel.getLocation() == null || hotel.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel location is required.");
        }

        if (hotel.getDescription() == null || hotel.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel description is required.");
        }

        return hotelDAO.updateHotel(hotel);
    }

    public boolean deleteHotel(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid hotel ID.");
        }

        Hotel hotel = hotelDAO.getHotelById(id);

        if (hotel == null) {
            throw new RuntimeException("Hotel not found with ID: " + id);
        }

        return hotelDAO.deleteHotel(id);
    }

    public List<Hotel> searchHotels(String keyword, int page, int size){

        int offset = (page - 1) * size;

        return hotelDAO.searchHotels(keyword, offset, size);
    }
}