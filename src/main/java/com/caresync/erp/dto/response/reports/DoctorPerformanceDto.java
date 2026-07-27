package com.caresync.erp.dto.response.reports;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorPerformanceDto {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private Long totalAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Double completionRate;
}
