package com.hotel.model;

public class DashboardStats {

    private int totalUsers;
    private int totalHotels;
    private int totalRooms;
    private int totalBookings;
    private double totalRevenue;

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public int getTotalHotels() { return totalHotels; }
    public void setTotalHotels(int totalHotels) { this.totalHotels = totalHotels; }

    public int getTotalRooms() { return totalRooms; }
    public void setTotalRooms(int totalRooms) { this.totalRooms = totalRooms; }

    public int getTotalBookings() { return totalBookings; }
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
}