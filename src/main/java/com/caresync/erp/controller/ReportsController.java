package com.caresync.erp.controller;

import com.caresync.erp.dto.response.reports.DailyAppointmentReportDto;
import com.caresync.erp.dto.response.reports.DoctorPerformanceDto;
import com.caresync.erp.dto.response.reports.GenderDistributionDto;
import com.caresync.erp.dto.response.reports.MonthlyAppointmentReportDto;
import com.caresync.erp.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;


    @GetMapping("/appointments/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DailyAppointmentReportDto> getDailyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyAppointmentReportDto report = reportsService.getDailyReport(date);
        return ResponseEntity.ok(report);
    }


    @GetMapping("/appointments/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonthlyAppointmentReportDto> getMonthlyReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        MonthlyAppointmentReportDto report = reportsService.getMonthlyReport(month);
        return ResponseEntity.ok(report);
    }


    @GetMapping("/patients/gender")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenderDistributionDto> getGenderDistribution() {
        GenderDistributionDto distribution = reportsService.getGenderDistribution();
        return ResponseEntity.ok(distribution);
    }


    @GetMapping("/doctors/performance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DoctorPerformanceDto>> getDoctorPerformance() {
        List<DoctorPerformanceDto> performance = reportsService.getDoctorPerformance();
        return ResponseEntity.ok(performance);
    }
}