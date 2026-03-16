package com.hotel.dao;

import com.hotel.model.DashboardStats;
import com.hotel.util.DBConnection;

import java.sql.*;

public class DashboardDAO {

    public DashboardStats getDashboardStats(){

        DashboardStats stats = new DashboardStats();

        try(Connection conn = DBConnection.getConnection()){

            Statement st = conn.createStatement();

            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM users");
            if(rs1.next()) stats.setTotalUsers(rs1.getInt(1));

            ResultSet rs2 = st.executeQuery("SELECT COUNT(*) FROM hotels");
            if(rs2.next()) stats.setTotalHotels(rs2.getInt(1));

            ResultSet rs3 = st.executeQuery("SELECT COUNT(*) FROM rooms");
            if(rs3.next()) stats.setTotalRooms(rs3.getInt(1));

            ResultSet rs4 = st.executeQuery("SELECT COUNT(*) FROM bookings");
            if(rs4.next()) stats.setTotalBookings(rs4.getInt(1));

            ResultSet rs5 = st.executeQuery("SELECT SUM(total_price) FROM bookings WHERE status='CONFIRMED'");
            if(rs5.next()) stats.setTotalRevenue(rs5.getDouble(1));

        }catch(Exception e){
            e.printStackTrace();
        }

        return stats;
    }
}