package com.caresync.erp.service;


import com.caresync.erp.dto.request.receptionist.ReceptionistBookAppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReceptionistAppointmentService {

    AppointmentResponseDto bookForWalkInPatient(ReceptionistBookAppointmentRequestDto requestDto);

    List<AppointmentResponseDto> getTodayAppointments();

    Page<AppointmentResponseDto> getAllAppointments(int page, int size, String sortBy, String direction);

    AppointmentResponseDto checkIn(Long appointmentId);

    AppointmentResponseDto checkOut(Long appointmentId);
}