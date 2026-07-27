package com.caresync.erp.dto.response.reports;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@Builder
public class MonthlyAppointmentReportDto {
    private YearMonth month;
    private Long totalAppointments;
    private Long booked;
    private Long completed;
    private Long cancelled;
    private List<DailySummaryDto> dailySummary;

    @Getter
    @Setter
    @Builder
    public static class DailySummaryDto {
        private Integer day;
        private Long count;
    }
}