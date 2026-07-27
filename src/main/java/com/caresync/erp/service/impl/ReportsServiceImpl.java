package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.dto.response.reports.DailyAppointmentReportDto;
import com.caresync.erp.dto.response.reports.DoctorPerformanceDto;
import com.caresync.erp.dto.response.reports.GenderDistributionDto;
import com.caresync.erp.dto.response.reports.MonthlyAppointmentReportDto;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.repository.projection.DailySummaryProjection;
import com.caresync.erp.repository.projection.DoctorPerformanceProjection;
import com.caresync.erp.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public DailyAppointmentReportDto getDailyReport(LocalDate date) {

        List<Appointment> appointments = appointmentRepository.findByAppointmentDate(date);

        long total = appointments.size();
        long booked = appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.BOOKED).count();
        long completed = appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count();
        long cancelled = appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count();

        List<DailyAppointmentReportDto.AppointmentDetailDto> details = appointments.stream()
                .map(a -> DailyAppointmentReportDto.AppointmentDetailDto.builder()
                        .id(a.getId())
                        .patientName(a.getPatient().getName())
                        .doctorName(a.getDoctor().getName())
                        .appointmentTime(a.getAppointmentTime().toString())
                        .status(a.getStatus().name())
                        .build())
                .collect(Collectors.toList());

        return DailyAppointmentReportDto.builder()
                .date(date)
                .totalAppointments(total)
                .booked(booked)
                .completed(completed)
                .cancelled(cancelled)
                .appointments(details)
                .build();
    }

    @Override
    public MonthlyAppointmentReportDto getMonthlyReport(YearMonth month) {
        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        long booked = appointmentRepository.countByAppointmentDateBetweenAndStatus(startDate, endDate, AppointmentStatus.BOOKED);
        long completed = appointmentRepository.countByAppointmentDateBetweenAndStatus(startDate, endDate, AppointmentStatus.COMPLETED);
        long cancelled = appointmentRepository.countByAppointmentDateBetweenAndStatus(startDate, endDate, AppointmentStatus.CANCELLED);
        long total = booked + completed + cancelled;

        List<Object[]> raw = appointmentRepository.findDailySummaryRaw(startDate, endDate);
        List<MonthlyAppointmentReportDto.DailySummaryDto> dailySummary = raw.stream()
                .map(row -> MonthlyAppointmentReportDto.DailySummaryDto.builder()
                        .day(((Number) row[0]).intValue())
                        .count(((Number) row[1]).longValue())
                        .build())
                .collect(Collectors.toList());

        return MonthlyAppointmentReportDto.builder()
                .month(month)
                .totalAppointments(total)
                .booked(booked)
                .completed(completed)
                .cancelled(cancelled)
                .dailySummary(dailySummary)
                .build();
    }

    @Override
    public GenderDistributionDto getGenderDistribution() {
        long male = patientRepository.countByGenderIgnoreCase("Male");
        long female = patientRepository.countByGenderIgnoreCase("Female");
        long total = patientRepository.count();
        long other = total - male - female;

        return GenderDistributionDto.builder()
                .male(male)
                .female(female)
                .other(other)
                .total(total)
                .build();
    }

    @Override
    public List<DoctorPerformanceDto> getDoctorPerformance() {

        List<DoctorPerformanceProjection> results = doctorRepository.findDoctorPerformance();

        return results.stream()
                .map(p -> {
                    long total = p.getTotal();
                    long completed = p.getCompleted() == null ? 0 : p.getCompleted();
                    long cancelled = p.getCancelled() == null ? 0 : p.getCancelled();

                    double completionRate = total > 0 ? (completed * 100.0 / total) : 0.0;

                    return DoctorPerformanceDto.builder()
                            .doctorId(p.getDoctorId())
                            .doctorName(p.getDoctorName())
                            .specialization(p.getSpecialization())
                            .totalAppointments(total)
                            .completedAppointments(completed)
                            .cancelledAppointments(cancelled)
                            .completionRate(Math.round(completionRate * 10) / 10.0)
                            .build();
                })
                .collect(Collectors.toList());
    }
}