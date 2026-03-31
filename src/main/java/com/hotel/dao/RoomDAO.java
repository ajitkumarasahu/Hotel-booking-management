package com.hotel.dao;

import com.hotel.model.Room;
import com.hotel.util.DBConnection;

import java.sql.*;
import java.util.*;

public class RoomDAO {

    public List<Room> getAllRooms() {

        List<Room> list = new ArrayList<>();

        String sql = "SELECT * FROM rooms";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Room room = new Room();

                room.setRoomId(rs.getLong("room_id"));
                room.setHotelId(rs.getLong("hotel_id"));
                room.setRoomType(rs.getString("room_type"));
                room.setPrice(rs.getBigDecimal("price"));
                room.setCapacity(rs.getInt("capacity"));
                room.setStatus(rs.getString("status"));

                list.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Room getRoomById(long roomId) {

        String sql = "SELECT * FROM rooms WHERE room_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, roomId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();

                    room.setRoomId(rs.getLong("room_id"));
                    room.setHotelId(rs.getLong("hotel_id"));
                    room.setRoomType(rs.getString("room_type"));
                    room.setPrice(rs.getBigDecimal("price"));
                    room.setCapacity(rs.getInt("capacity"));
                    room.setStatus(rs.getString("status"));

                    return room;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createRoom(Room room) {

        String sql = "INSERT INTO rooms (hotel_id, room_type, price, capacity, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn =  DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, room.getHotelId());
            ps.setString(2, room.getRoomType());
            ps.setBigDecimal(3, room.getPrice());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean updateRoom(Room room) {

        String sql = "UPDATE rooms SET hotel_id=?, room_type=?, price=?, capacity=?, status=? WHERE room_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, room.getHotelId());
            ps.setString(2, room.getRoomType());
            ps.setBigDecimal(3, room.getPrice());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getStatus());
            ps.setLong(6, room.getRoomId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRoom(long roomId) {

        String sql = "DELETE FROM rooms WHERE room_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, roomId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public double getRoomPrice(long roomId) {

        String sql = "SELECT price FROM rooms WHERE room_id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, roomId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                return rs.getDouble("price");
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    public List<Room> getRoomsByHotelId(long hotelId){

        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM rooms WHERE hotel_id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1,hotelId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                Room r = new Room();

                r.setRoomId(rs.getLong("room_id"));
                r.setHotelId(rs.getLong("hotel_id"));
                r.setRoomType(rs.getString("room_type"));
                r.setPrice(rs.getBigDecimal("price"));
                r.setCapacity(rs.getInt("capacity"));
                r.setStatus(rs.getString("status"));

                rooms.add(r);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return rooms;
    }

    public boolean bulkInsertRooms(List<Room> rooms){

        String sql = "INSERT INTO rooms (hotel_id, room_type, price, capacity, status) VALUES(?,?,?,?,?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            conn.setAutoCommit(false);

            for(Room r : rooms){

                ps.setLong(1,r.getHotelId());
                ps.setString(2,r.getRoomType());
                ps.setBigDecimal(3,r.getPrice());
                ps.setInt(4,r.getCapacity());
                ps.setString(5,r.getStatus());

                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
}