package com.hotel.service;

import com.hotel.dao.RoomDAO;
import com.hotel.model.Room;

import java.util.List;

public class RoomService {

    private RoomDAO roomDAO = new RoomDAO();

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public Room getRoomById(long roomId) {
        return roomDAO.getRoomById(roomId);
    }

    public boolean addRoom(Room room) {

        if (room.getPrice().doubleValue() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }

        return roomDAO.createRoom(room);
    }

    public boolean updateRoom(Room room) {
        return roomDAO.updateRoom(room);
    }

    public boolean deleteRoom(long roomId) {
        return roomDAO.deleteRoom(roomId);
    }

    public List<Room> getRoomsByHotel(long hotelId){
        return roomDAO.getRoomsByHotelId(hotelId);
    }

    public boolean bulkAddRooms(List<Room> rooms){

        if(rooms == null || rooms.isEmpty()){
            throw new RuntimeException("Room list empty");
        }

        return roomDAO.bulkInsertRooms(rooms);
    }
}