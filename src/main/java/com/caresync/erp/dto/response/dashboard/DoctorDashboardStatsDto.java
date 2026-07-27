package com.caresync.erp.dto.response.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorDashboardStatsDto {
    private Long totalPatients;
    private Long todayAppointments;
    private Long completedToday;
    private Long upcomingAppointments;
    private Long pendingAppointments;
}