package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.dto.request.appointment.AppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.mapper.AppointmentMapper;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public AppointmentResponseDto createAppointments(AppointmentRequestDto requestDto) {
        log.info("Creating appointment: doctorId={}, patientId={}, date={}, time={}",
                requestDto.getDoctorId(), requestDto.getPatientId(),
                requestDto.getAppointmentDate(), requestDto.getAppointmentTime());

        Doctor doctor = doctorRepository.findByIdAndActiveTrue(requestDto.getDoctorId())
                .orElseThrow(() -> {
                    log.warn("Appointment creation failed — doctor not found: doctorId={}", requestDto.getDoctorId());
                    return new ResourceNotFoundException("Doctor not found with id: " + requestDto.getDoctorId());
                });

        Patient patient = patientRepository.findByIdAndActiveTrue(requestDto.getPatientId())
                .orElseThrow(() -> {
                    log.warn("Appointment creation failed — patient not found: patientId={}", requestDto.getPatientId());
                    return new ResourceNotFoundException("Patient not found with id: " + requestDto.getPatientId());
                });

        boolean slotAlreadyBooked = appointmentRepository.existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndActiveTrue(
                doctor.getId(),
                requestDto.getAppointmentDate(),
                requestDto.getAppointmentTime()
        );

        if (slotAlreadyBooked) {
            log.warn("Appointment creation failed — slot already booked: doctorId={}, date={}, time={}",
                    doctor.getId(), requestDto.getAppointmentDate(), requestDto.getAppointmentTime());
            throw new IllegalArgumentException("Appointment slot already booked for this doctor");
        }

        Appointment appointment = AppointmentMapper.mapToAppointmentEntity(requestDto, doctor, patient);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment created successfully: appointmentId={}", savedAppointment.getId());
        return AppointmentMapper.mapToAppointmentResponseDto(savedAppointment);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndActiveTrue(appointmentId)
                .orElseThrow(() -> {
                    log.warn("Appointment not found: appointmentId={}", appointmentId);
                    return new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
                });
        return AppointmentMapper.mapToAppointmentResponseDto(appointment);
    }

    @Override
    public Page<AppointmentResponseDto> getAppointmentByDoctor(Long doctorId, int page, int size, String sortBy, String direction) {
        if (!doctorRepository.existsById(doctorId)) {
            log.warn("Fetch appointments failed — doctor not found: doctorId={}", doctorId);
            throw new ResourceNotFoundException("Doctor Not Found with id: " + doctorId);
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return appointmentRepository.findByDoctor_IdAndActiveTrue(doctorId, pageable)
                .map(AppointmentMapper::mapToAppointmentResponseDto);
    }

    @Override
    public Page<AppointmentResponseDto> getAppointmentsByPatient(Long patientId, int page, int size, String sortBy, String direction) {
        if (!patientRepository.existsById(patientId)) {
            log.warn("Fetch appointments failed — patient not found: patientId={}", patientId);
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return appointmentRepository.findByPatient_IdAndActiveTrue(patientId, pageable)
                .map(AppointmentMapper::mapToAppointmentResponseDto);
    }

    @Override
    public AppointmentResponseDto cancelAppointments(Long appointmentId) {
        log.info("Cancelling appointment: appointmentId={}", appointmentId);

        Appointment appointment = appointmentRepository.findByIdAndActiveTrue(appointmentId)
                .orElseThrow(() -> {
                    log.warn("Cancel failed — appointment not found: appointmentId={}", appointmentId);
                    return new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
                });

        appointment.setStatus(AppointmentStatus.CANCELLED);
        AppointmentResponseDto response = AppointmentMapper.mapToAppointmentResponseDto(appointmentRepository.save(appointment));
        log.info("Appointment cancelled successfully: appointmentId={}", appointmentId);
        return response;
    }

    @Override
    public AppointmentResponseDto completeAppointment(Long appointmentId) {
        log.info("Completing appointment: appointmentId={}", appointmentId);

        Appointment appointment = appointmentRepository.findByIdAndActiveTrue(appointmentId)
                .orElseThrow(() -> {
                    log.warn("Complete failed — appointment not found: appointmentId={}", appointmentId);
                    return new ResourceNotFoundException("Appointment not found with id: " + appointmentId);
                });

        appointment.setStatus(AppointmentStatus.COMPLETED);
        AppointmentResponseDto response = AppointmentMapper.mapToAppointmentResponseDto(appointmentRepository.save(appointment));
        log.info("Appointment completed successfully: appointmentId={}", appointmentId);
        return response;
    }

    @Override
    public Page<AppointmentResponseDto> getAllAppointments(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return appointmentRepository.findAll(pageable)
                .map(AppointmentMapper::mapToAppointmentResponseDto);
    }
}