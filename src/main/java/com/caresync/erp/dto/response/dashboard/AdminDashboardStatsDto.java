package com.caresync.erp.dto.response.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminDashboardStatsDto {
    private Long totalDoctors;
    private Long activeDoctors;
    private Long totalPatients;
    private Long activePatients;
    private Long totalAppointments;
    private Long todayAppointments;
    private Long completedAppointments;
    private Long pendingAppointments;
    private Long cancelledAppointments;
    private Double averageExperience;
}