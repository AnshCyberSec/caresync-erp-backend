package com.caresync.erp.dto.response.dashboard;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReceptionistDashboardStatsDto {
    private Long todayAppointments;
    private Long checkedIn;
    private Long waiting;
    private Long newRegistrations;
    private Long totalPatients;
}