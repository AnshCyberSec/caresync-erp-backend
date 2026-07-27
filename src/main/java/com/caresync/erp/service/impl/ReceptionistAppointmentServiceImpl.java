package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.dto.request.receptionist.ReceptionistBookAppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.mapper.AppointmentMapper;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.ReceptionistAppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceptionistAppointmentServiceImpl implements ReceptionistAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public AppointmentResponseDto bookForWalkInPatient(ReceptionistBookAppointmentRequestDto requestDto) {
        log.info("Receptionist booking walk-in appointment: doctorId={}, patientId={}, phone={}",
                requestDto.getDoctorId(), requestDto.getPatientId(), requestDto.getPatientPhone());

        Doctor doctor = doctorRepository.findByIdAndActiveTrue(requestDto.getDoctorId())
                .orElseThrow(() -> {
                    log.warn("Walk-in booking failed — doctor not found: doctorId={}", requestDto.getDoctorId());
                    return new ResourceNotFoundException("Doctor not found with id: " + requestDto.getDoctorId());
                });

        Patient patient = resolvePatient(requestDto);

        boolean slotAlreadyBooked = appointmentRepository.existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndActiveTrue(
                doctor.getId(),
                requestDto.getAppointmentDate(),
                requestDto.getAppointmentTime()
        );

        if (slotAlreadyBooked) {
            log.warn("Walk-in booking failed — slot already booked: doctorId={}, date={}, time={}",
                    doctor.getId(), requestDto.getAppointmentDate(), requestDto.getAppointmentTime());
            throw new IllegalArgumentException("Appointment slot already booked for this doctor");
        }

        Appointment appointment = AppointmentMapper.mapToWalkInAppointmentEntity(
                doctor, patient, requestDto.getAppointmentDate(), requestDto.getAppointmentTime());

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Walk-in appointment booked: appointmentId={}, patientId={}", saved.getId(), patient.getId());

        return AppointmentMapper.mapToAppointmentResponseDto(saved);
    }


    private Patient resolvePatient(ReceptionistBookAppointmentRequestDto requestDto) {

        // Case 1: Receptionist ne existing patient select kiya
        if (requestDto.getPatientId() != null) {
            return patientRepository.findByIdAndActiveTrue(requestDto.getPatientId())
                    .orElseThrow(() -> {
                        log.warn("Walk-in booking failed — given patientId not found: {}", requestDto.getPatientId());
                        return new ResourceNotFoundException("Patient not found with id: " + requestDto.getPatientId());
                    });
        }

        // Case 2: Phone number se dekho patient pehle se registered to nahi
        if (requestDto.getPatientPhone() == null || requestDto.getPatientPhone().isBlank()) {
            throw new IllegalArgumentException("Patient phone is required for walk-in booking");
        }

        return patientRepository.findByPhoneAndActiveTrue(requestDto.getPatientPhone())
                .orElseGet(() -> createWalkInPatient(requestDto));
    }

    private Patient createWalkInPatient(ReceptionistBookAppointmentRequestDto requestDto) {
        if (requestDto.getPatientName() == null || requestDto.getPatientName().isBlank()) {
            throw new IllegalArgumentException("Patient name is required for new walk-in patient");
        }
        if (requestDto.getPatientAge() == null) {
            throw new IllegalArgumentException("Patient age is required for new walk-in patient");
        }
        if (requestDto.getPatientGender() == null || requestDto.getPatientGender().isBlank()) {
            throw new IllegalArgumentException("Patient gender is required for new walk-in patient");
        }


        String email = (requestDto.getPatientEmail() != null && !requestDto.getPatientEmail().isBlank())
                ? requestDto.getPatientEmail()
                : "walkin_" + requestDto.getPatientPhone() + "@no-email.caresync.local";

        Patient patient = Patient.builder()
                .name(requestDto.getPatientName())
                .age(requestDto.getPatientAge())
                .gender(requestDto.getPatientGender())
                .phone(requestDto.getPatientPhone())
                .email(email)
                .address(requestDto.getPatientAddress())
                .active(true)
                .build();

        Patient saved = patientRepository.save(patient);
        log.info("New walk-in patient created: patientId={}, phone={}", saved.getId(), saved.getPhone());
        return saved;
    }

    @Override
    public List<AppointmentResponseDto> getTodayAppointments() {
        LocalDate today = LocalDate.now();
        List<Appointment> appointments =
                appointmentRepository.findByAppointmentDateAndActiveTrueOrderByAppointmentTimeAsc(today);

        return appointments.stream()
                .map(AppointmentMapper::mapToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AppointmentResponseDto> getAllAppointments(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return appointmentRepository.findByActiveTrue(pageable)
                .map(AppointmentMapper::mapToAppointmentResponseDto);
    }

    @Override
    @Transactional
    public AppointmentResponseDto checkIn(Long appointmentId) {
        log.info("Receptionist checking in appointment: appointmentId={}", appointmentId);

        Appointment appointment = appointmentRepository.findByIdAndActiveTrue(appointmentId)
                .orElseThrow(() -> {
                    log.warn("Check-in failed — appointment not found: appointmentId={}", appointmentId);
                    return new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
                });

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            log.warn("Check-in failed — appointment not in BOOKED status: appointmentId={}, status={}",
                    appointmentId, appointment.getStatus());
            throw new IllegalArgumentException("Only booked appointments can be checked in. Current status: "
                    + appointment.getStatus());
        }

        if (!appointment.getAppointmentDate().equals(LocalDate.now())) {
            log.warn("Check-in failed — appointment is not for today: appointmentId={}, date={}",
                    appointmentId, appointment.getAppointmentDate());
            throw new IllegalArgumentException("Only today's appointments can be checked in");
        }

        appointment.setStatus(AppointmentStatus.CHECKED_IN);
        appointment.setCheckInTime(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment checked in successfully: appointmentId={}", appointmentId);

        return AppointmentMapper.mapToAppointmentResponseDto(saved);
    }

    @Override
    @Transactional
    public AppointmentResponseDto checkOut(Long appointmentId) {
        log.info("Receptionist checking out appointment: appointmentId={}", appointmentId);

        Appointment appointment = appointmentRepository.findByIdAndActiveTrue(appointmentId)
                .orElseThrow(() -> {
                    log.warn("Check-out failed — appointment not found: appointmentId={}", appointmentId);
                    return new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
                });

        if (appointment.getStatus() != AppointmentStatus.CHECKED_IN) {
            log.warn("Check-out failed — appointment not in CHECKED_IN status: appointmentId={}, status={}",
                    appointmentId, appointment.getStatus());
            throw new IllegalArgumentException("Only checked-in appointments can be checked out. Current status: "
                    + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setCheckOutTime(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment checked out (completed) successfully: appointmentId={}", appointmentId);

        return AppointmentMapper.mapToAppointmentResponseDto(saved);
    }
}