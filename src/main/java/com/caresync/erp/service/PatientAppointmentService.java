package com.caresync.erp.service;

import com.caresync.erp.dto.request.patient.PatientBookAppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.dto.response.patient.PatientAppointmentResponseDto;

import java.util.List;

public interface PatientAppointmentService {
    List<PatientAppointmentResponseDto> getPatientAppointments(Long patientId);
    AppointmentResponseDto bookAppointment(PatientBookAppointmentRequestDto requestDto);
    AppointmentResponseDto cancelAppointment(Long appointmentId, Long patientId);
}

