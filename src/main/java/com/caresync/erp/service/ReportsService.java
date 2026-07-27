package com.caresync.erp.service;

import com.caresync.erp.dto.response.reports.DailyAppointmentReportDto;
import com.caresync.erp.dto.response.reports.DoctorPerformanceDto;
import com.caresync.erp.dto.response.reports.GenderDistributionDto;
import com.caresync.erp.dto.response.reports.MonthlyAppointmentReportDto;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface ReportsService {
    DailyAppointmentReportDto getDailyReport(LocalDate date);

    MonthlyAppointmentReportDto getMonthlyReport(YearMonth month);

    GenderDistributionDto getGenderDistribution();

    List<DoctorPerformanceDto> getDoctorPerformance();
}