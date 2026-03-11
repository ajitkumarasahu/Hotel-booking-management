package com.hotel.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Booking {

    private long bookingId;
    private long userId;
    private long roomId;
    private Date checkIn;
    private Date checkOut;
    private BigDecimal totalPrice;
    private String status;

    public Booking(){}

    public Booking(long userId,long roomId,Date checkIn,Date checkOut,BigDecimal totalPrice,String status){
        this.userId=userId;
        this.roomId=roomId;
        this.checkIn=checkIn;
        this.checkOut=checkOut;
        this.totalPrice=totalPrice;
        this.status=status;
    }

    public long getBookingId(){ return bookingId; }
    public void setBookingId(long bookingId){ this.bookingId=bookingId; }

    public long getUserId(){ return userId; }
    public void setUserId(long userId){ this.userId=userId; }

    public long getRoomId(){ return roomId; }
    public void setRoomId(long roomId){ this.roomId=roomId; }

    public Date getCheckIn(){ return checkIn; }
    public void setCheckIn(Date checkIn){ this.checkIn=checkIn; }

    public Date getCheckOut(){ return checkOut; }
    public void setCheckOut(Date checkOut){ this.checkOut=checkOut; }

    public BigDecimal getTotalPrice(){ return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice){ this.totalPrice=totalPrice; }

    public String getStatus(){ return status; }
    public void setStatus(String status){ this.status=status; }
}