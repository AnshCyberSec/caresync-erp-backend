package com.caresync.erp.controller;

import com.caresync.erp.dto.response.dashboard.AdminDashboardStatsDto;
import com.caresync.erp.dto.response.dashboard.DoctorDashboardStatsDto;
import com.caresync.erp.dto.response.dashboard.ReceptionistDashboardStatsDto;
import com.caresync.erp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardStatsDto> getAdminDashboardStats() {
        AdminDashboardStatsDto stats = dashboardService.getAdminDashboardStats();
        return ResponseEntity.ok(stats);
    }


    @GetMapping("/doctor/stats/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDashboardStatsDto> getDoctorDashboardStats(@PathVariable Long doctorId) {
        DoctorDashboardStatsDto stats = dashboardService.getDoctorDashboardStats(doctorId);
        return ResponseEntity.ok(stats);
    }


    @GetMapping("/receptionist/stats")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<ReceptionistDashboardStatsDto> getReceptionistDashboardStats() {
        ReceptionistDashboardStatsDto stats = dashboardService.getReceptionistDashboardStats();
        return ResponseEntity.ok(stats);
    }
}
