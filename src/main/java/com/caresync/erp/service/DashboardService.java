package com.caresync.erp.service;

import com.caresync.erp.dto.response.dashboard.AdminDashboardStatsDto;
import com.caresync.erp.dto.response.dashboard.DoctorDashboardStatsDto;
import com.caresync.erp.dto.response.dashboard.ReceptionistDashboardStatsDto;

public interface DashboardService {
    AdminDashboardStatsDto getAdminDashboardStats();

    DoctorDashboardStatsDto getDoctorDashboardStats(Long doctorId);

    ReceptionistDashboardStatsDto getReceptionistDashboardStats();

}
