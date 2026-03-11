package com.hotel.dao;

import com.hotel.model.Booking;
import com.hotel.util.DBConnection;

import java.sql.*;
import java.util.*;

public class BookingDAO {

    public List<Booking> getAllBookings(){

        List<Booking> list=new ArrayList<>();

        String sql="SELECT * FROM bookings";

        try(Connection conn=DBConnection.getConnection();
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(sql)){

            while(rs.next()){

                Booking b=new Booking();

                b.setBookingId(rs.getLong("booking_id"));
                b.setUserId(rs.getLong("user_id"));
                b.setRoomId(rs.getLong("room_id"));
                b.setCheckIn(rs.getDate("check_in"));
                b.setCheckOut(rs.getDate("check_out"));
                b.setTotalPrice(rs.getBigDecimal("total_price"));
                b.setStatus(rs.getString("status"));

                list.add(b);
            }

        }catch(Exception e){e.printStackTrace();}

        return list;
    }

    public Booking getBookingById(long bookingId){

        String sql="SELECT * FROM bookings WHERE booking_id=?";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setLong(1,bookingId);

            ResultSet rs=ps.executeQuery();

            if(rs.next()){

                Booking b=new Booking();

                b.setBookingId(rs.getLong("booking_id"));
                b.setUserId(rs.getLong("user_id"));
                b.setRoomId(rs.getLong("room_id"));
                b.setCheckIn(rs.getDate("check_in"));
                b.setCheckOut(rs.getDate("check_out"));
                b.setTotalPrice(rs.getBigDecimal("total_price"));
                b.setStatus(rs.getString("status"));

                return b;
            }

        }catch(Exception e){e.printStackTrace();}

        return null;
    }

    public boolean createBooking(Booking booking){

        String sql="INSERT INTO bookings(user_id,room_id,check_in,check_out,total_price,status) VALUES(?,?,?,?,?,?)";

        try(Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setLong(1,booking.getUserId());
            ps.setLong(2,booking.getRoomId());
            ps.setDate(3,booking.getCheckIn());
            ps.setDate(4,booking.getCheckOut());
            ps.setBigDecimal(5,booking.getTotalPrice());
            ps.setString(6,booking.getStatus());

            return ps.executeUpdate()>0;

        }catch(Exception e){e.printStackTrace();}

        return false;
    }

    public boolean updateBooking(Booking booking){

        String sql="UPDATE bookings SET user_id=?,room_id=?,check_in=?,check_out=?,total_price=?,status=? WHERE booking_id=?";

        try(Connection conn= DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setLong(1,booking.getUserId());
            ps.setLong(2,booking.getRoomId());
            ps.setDate(3,booking.getCheckIn());
            ps.setDate(4,booking.getCheckOut());
            ps.setBigDecimal(5,booking.getTotalPrice());
            ps.setString(6,booking.getStatus());
            ps.setLong(7,booking.getBookingId());

            return ps.executeUpdate()>0;

        }catch(Exception e){e.printStackTrace();}

        return false;
    }

    public boolean deleteBooking(long bookingId){

        String sql="DELETE FROM bookings WHERE booking_id=?";

        try(Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){

            ps.setLong(1,bookingId);

            return ps.executeUpdate()>0;

        }catch(Exception e){e.printStackTrace();}

        return false;
    }
}