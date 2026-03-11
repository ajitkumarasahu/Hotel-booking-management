package com.hotel.dao;

import com.hotel.model.Hotel;
import com.hotel.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // GET ALL HOTELS
    public List<Hotel> getAllHotels(){

        List<Hotel> list = new ArrayList<>();
        String sql = "SELECT * FROM hotels";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()){

                Hotel h = new Hotel();
                h.setHotelId(rs.getInt("hotel_id"));
                h.setName(rs.getString("name"));
                h.setLocation(rs.getString("location"));
                h.setDescription(rs.getString("description"));

                list.add(h);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    // GET HOTEL BY ID
    public Hotel getHotelById(int id){
        String sql = "SELECT * FROM hotels WHERE hotel_id=?";
        Hotel h = null;

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    h = new Hotel();
                    h.setHotelId(rs.getInt("hotel_id"));
                    h.setName(rs.getString("name"));
                    h.setLocation(rs.getString("location"));
                    h.setDescription(rs.getString("description"));
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return h;
    }

    // CREATE HOTEL
    public boolean addHotel(Hotel hotel) {

        String sql = "INSERT INTO hotels(name, location, description) VALUES(?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hotel.getName());
            ps.setString(2, hotel.getLocation());
            ps.setString(3, hotel.getDescription());

            return ps.executeUpdate() > 0;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    
    // UPDATE HOTEL
    public boolean updateHotel(Hotel hotel){

        String sql = "UPDATE hotels SET name=?, location=?, description=? WHERE hotel_id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hotel.getName());
            ps.setString(2, hotel.getLocation());
            ps.setString(3, hotel.getDescription());
            ps.setInt(4, hotel.getHotelId());

            return ps.executeUpdate() > 0;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    // DELETE HOTEL
    public boolean deleteHotel(int id){

        String sql = "DELETE FROM hotels WHERE hotel_id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
}