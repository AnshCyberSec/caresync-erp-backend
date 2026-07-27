package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.dto.request.patient.PatientBookAppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.dto.response.patient.PatientAppointmentResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.mapper.AppointmentMapper;
import com.caresync.erp.mapper.PatientAppointmentMapper;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.PatientAppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientAppointmentServiceImpl implements PatientAppointmentService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public List<PatientAppointmentResponseDto> getPatientAppointments(Long patientId) {
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> {
                    log.warn("Fetch failed — patient not found: patientId={}", patientId);
                    return new ResourceNotFoundException("Patient not found with id: " + patientId);
                });

        Pageable pageable = PageRequest.of(0, 100, Sort.by("appointmentDate").descending());
        List<Appointment> appointments = appointmentRepository.findByPatient_IdAndActiveTrue(patientId, pageable)
                .getContent();

        return appointments.stream()
                .map(appointment -> {
                    Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                            .orElse(null);
                    return PatientAppointmentMapper.mapToResponseDto(appointment, doctor);
                })
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto bookAppointment(PatientBookAppointmentRequestDto requestDto) {
        log.info("Patient booking appointment: patientId={}, doctorId={}, date={}, time={}",
                requestDto.getPatientId(), requestDto.getDoctorId(),
                requestDto.getAppointmentDate(), requestDto.getAppointmentTime());

        Doctor doctor = doctorRepository.findByIdAndActiveTrue(requestDto.getDoctorId())
                .orElseThrow(() -> {
                    log.warn("Booking failed — doctor not found: doctorId={}", requestDto.getDoctorId());
                    return new ResourceNotFoundException("Doctor not found with id: " + requestDto.getDoctorId());
                });

        Patient patient = patientRepository.findByIdAndActiveTrue(requestDto.getPatientId())
                .orElseThrow(() -> {
                    log.warn("Booking failed — patient not found: patientId={}", requestDto.getPatientId());
                    return new ResourceNotFoundException("Patient not found with id: " + requestDto.getPatientId());
                });

        boolean slotAlreadyBooked = appointmentRepository.existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndActiveTrue(
                doctor.getId(),
                requestDto.getAppointmentDate(),
                requestDto.getAppointmentTime()
        );

        if (slotAlreadyBooked) {
            log.warn("Booking failed — slot already booked: doctorId={}, date={}, time={}",
                    doctor.getId(), requestDto.getAppointmentDate(), requestDto.getAppointmentTime());
            throw new IllegalArgumentException("Appointment slot already booked for this doctor");
        }

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(requestDto.getAppointmentDate())
                .appointmentTime(requestDto.getAppointmentTime())
                .status(AppointmentStatus.BOOKED)
                .active(true)
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);
        log.info("Appointment booked successfully: appointmentId={}, patientId={}",
                savedAppointment.getId(), requestDto.getPatientId());

        return AppointmentMapper.mapToAppointmentResponseDto(savedAppointment);
    }

    @Override
    public AppointmentResponseDto cancelAppointment(Long appointmentId, Long patientId) {
        log.info("Patient cancelling appointment: appointmentId={}, patientId={}", appointmentId, patientId);

        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> {
                    log.warn("Cancel failed — patient not found: patientId={}", patientId);
                    return new ResourceNotFoundException("Patient not found with id: " + patientId);
                });

        Appointment appointment = appointmentRepository.findByIdAndActiveTrue(appointmentId)
                .orElseThrow(() -> {
                    log.warn("Cancel failed — appointment not found: appointmentId={}", appointmentId);
                    return new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
                });

        if (appointment.getPatient().getId() != patientId) {
            log.warn("Cancel failed — appointment does not belong to patient: appointmentId={}, patientId={}",
                    appointmentId, patientId);
            throw new IllegalArgumentException("This appointment does not belong to you");
        }

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            log.warn("Cancel failed — appointment not in BOOKED status: appointmentId={}, status={}",
                    appointmentId, appointment.getStatus());
            throw new IllegalArgumentException("Only booked appointments can be cancelled");
        }

        LocalDate today = LocalDate.now();
        if (appointment.getAppointmentDate().isBefore(today)) {
            log.warn("Cancel failed — appointment is in the past: appointmentId={}", appointmentId);
            throw new IllegalArgumentException("Cannot cancel past appointments");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment cancelledAppointment = appointmentRepository.save(appointment);

        log.info("Appointment cancelled successfully by patient: appointmentId={}, patientId={}",
                appointmentId, patientId);

        return AppointmentMapper.mapToAppointmentResponseDto(cancelledAppointment);
    }
}