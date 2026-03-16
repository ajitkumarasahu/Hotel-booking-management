package com.hotel.service;

import com.hotel.dao.DashboardDAO;
import com.hotel.model.DashboardStats;

public class DashboardService {

    private DashboardDAO dashboardDAO = new DashboardDAO();

    public DashboardStats getStats(){
        return dashboardDAO.getDashboardStats();
    }
}