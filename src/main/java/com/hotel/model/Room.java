package com.hotel.model;

import java.math.BigDecimal;

public class Room {

    private long roomId;
    private long hotelId;
    private String roomType;
    private BigDecimal price;
    private int capacity;
    private String status;

    public Room() {}

    public Room(long hotelId, String roomType, BigDecimal price, int capacity, String status) {
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.price = price;
        this.capacity = capacity;
        this.status = status;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getHotelId() {
        return hotelId;
    }

    public void setHotelId(long hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}