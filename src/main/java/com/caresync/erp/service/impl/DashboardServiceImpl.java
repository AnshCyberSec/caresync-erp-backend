package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.dto.response.dashboard.AdminDashboardStatsDto;
import com.caresync.erp.dto.response.dashboard.DoctorDashboardStatsDto;
import com.caresync.erp.dto.response.dashboard.ReceptionistDashboardStatsDto;

import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public AdminDashboardStatsDto getAdminDashboardStats() {
        long totalDoctors = doctorRepository.count();
        long activeDoctors = doctorRepository.countByActiveTrue();

        long totalPatients = patientRepository.count();
        long activePatients = patientRepository.countByActiveTrue();

        long totalAppointments = appointmentRepository.count();

        LocalDate today = LocalDate.now();
        long todayAppointments = appointmentRepository.countByAppointmentDateAndActiveTrue(today);

        long completedAppointments = appointmentRepository.countByStatusAndActiveTrue(AppointmentStatus.COMPLETED);
        long pendingAppointments = appointmentRepository.countByStatusAndActiveTrue(AppointmentStatus.BOOKED);
        long cancelledAppointments = appointmentRepository.countByStatusAndActiveTrue(AppointmentStatus.CANCELLED);

        Double avgExperience = doctorRepository.findAverageExperienceYears();
        double roundedAvg = avgExperience == null ? 0.0 : Math.round(avgExperience * 10) / 10.0;

        return AdminDashboardStatsDto.builder()
                .totalDoctors(totalDoctors)
                .activeDoctors(activeDoctors)
                .totalPatients(totalPatients)
                .activePatients(activePatients)
                .totalAppointments(totalAppointments)
                .todayAppointments(todayAppointments)
                .completedAppointments(completedAppointments)
                .pendingAppointments(pendingAppointments)
                .cancelledAppointments(cancelledAppointments)
                .averageExperience(roundedAvg)
                .build();
    }

    @Override
    public DoctorDashboardStatsDto getDoctorDashboardStats(Long doctorId) {
        LocalDate today = LocalDate.now();

        long totalPatients = appointmentRepository.countDistinctPatientsByDoctorId(doctorId);
        long todayAppointments = appointmentRepository.countByDoctor_IdAndAppointmentDateAndActiveTrue(doctorId, today);
        long completedToday = appointmentRepository.countByDoctor_IdAndAppointmentDateAndStatusAndActiveTrue(
                doctorId, today, AppointmentStatus.COMPLETED);
        long upcomingAppointments = appointmentRepository.countByDoctor_IdAndAppointmentDateAfterAndStatusAndActiveTrue(
                doctorId, today, AppointmentStatus.BOOKED);
        long pendingAppointments = appointmentRepository.countByDoctor_IdAndStatusAndActiveTrue(
                doctorId, AppointmentStatus.BOOKED);

        return DoctorDashboardStatsDto.builder()
                .totalPatients(totalPatients)
                .todayAppointments(todayAppointments)
                .completedToday(completedToday)
                .upcomingAppointments(upcomingAppointments)
                .pendingAppointments(pendingAppointments)
                .build();
    }

    @Override
    public ReceptionistDashboardStatsDto getReceptionistDashboardStats() {
        long totalPatients = patientRepository.count();

        LocalDate today = LocalDate.now();
        long newRegistrations = patientRepository.countByRegistrationDate(today);

        long todayAppointments = appointmentRepository.countByAppointmentDateAndActiveTrue(today);


        long checkedIn = appointmentRepository.countByAppointmentDateAndStatusAndActiveTrue(
                today, AppointmentStatus.CHECKED_IN);
        long completedToday = appointmentRepository.countByAppointmentDateAndStatusAndActiveTrue(
                today, AppointmentStatus.COMPLETED);
        long waiting = todayAppointments - checkedIn - completedToday;

        return ReceptionistDashboardStatsDto.builder()
                .todayAppointments(todayAppointments)
                .checkedIn(checkedIn)
                .waiting(Math.max(waiting, 0))
                .newRegistrations(newRegistrations)
                .totalPatients(totalPatients)
                .build();
    }
}