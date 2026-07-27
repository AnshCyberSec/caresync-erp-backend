package com.caresync.erp.service;

import com.caresync.erp.dto.request.appointment.AppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    AppointmentResponseDto createAppointments(AppointmentRequestDto requestDto);

    AppointmentResponseDto getAppointmentById(Long appointmentId);

    Page<AppointmentResponseDto> getAppointmentByDoctor(
            Long doctorId,int page,int size,String sortBy,String direction);

    Page<AppointmentResponseDto> getAppointmentsByPatient(
            Long patientId, int page, int size, String sortBy, String direction
    );

    AppointmentResponseDto cancelAppointments(Long appointmentId);

    AppointmentResponseDto completeAppointment(Long appointmentId);

    Page<AppointmentResponseDto> getAllAppointments(int page, int size, String sortBy, String direction);



}

