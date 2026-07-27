package com.caresync.erp.dto.response.reports;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class DailyAppointmentReportDto {
    private LocalDate date;
    private Long totalAppointments;
    private Long booked;
    private Long completed;
    private Long cancelled;
    private List<AppointmentDetailDto> appointments;

    @Getter
    @Setter
    @Builder
    public static class AppointmentDetailDto {
        private Long id;
        private String patientName;
        private String doctorName;
        private String appointmentTime;
        private String status;
    }
}
