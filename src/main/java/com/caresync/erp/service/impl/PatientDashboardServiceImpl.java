package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.dto.response.patient.PatientDashboardDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.getPatientDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientDashboardServiceImpl implements getPatientDashboard {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public PatientDashboardDto getPatientDashboard(Long patientId) {
        // Fetch patient details
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        // Fetch all appointments for this patient
        Pageable pageable = PageRequest.of(0, 100, Sort.by("appointmentDate").descending());
        List<Appointment> appointments = appointmentRepository.findByPatient_IdAndActiveTrue(patientId, pageable)
                .getContent();

        // Calculate statistics
        LocalDate today = LocalDate.now();

        int totalAppointments = appointments.size();
        int upcomingAppointments = (int) appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.BOOKED &&
                        (a.getAppointmentDate().isAfter(today) || a.getAppointmentDate().equals(today)))
                .count();
        int completedAppointments = (int) appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                .count();
        int cancelledAppointments = (int) appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CANCELLED)
                .count();

        // Get recent appointments (last 5)
        List<PatientDashboardDto.PatientAppointmentDto> recentAppointments = appointments.stream()
                .limit(5)
                .map(this::mapToAppointmentDto)
                .collect(Collectors.toList());

        return PatientDashboardDto.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .age(patient.getAge())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .active(patient.getActive())
                .totalAppointments(totalAppointments)
                .upcomingAppointments(upcomingAppointments)
                .completedAppointments(completedAppointments)
                .cancelledAppointments(cancelledAppointments)
                .recentAppointments(recentAppointments)
                .build();
    }

    private PatientDashboardDto.PatientAppointmentDto mapToAppointmentDto(Appointment appointment) {
        // Get doctor details
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElse(null);

        return PatientDashboardDto.PatientAppointmentDto.builder()
                .appointmentId(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(doctor != null ? doctor.getName() : "Unknown Doctor")
                .doctorSpecialization(doctor != null ? doctor.getSpecialization() : "N/A")
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime() != null ?
                        appointment.getAppointmentTime().toString() : null)
                .status(appointment.getStatus().name())
                .build();
    }
}