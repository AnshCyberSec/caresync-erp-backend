package com.caresync.erp.controller;

import com.caresync.erp.dto.request.patient.PatientBookAppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.dto.response.patient.PatientAppointmentResponseDto;
import com.caresync.erp.security.jwt.JwtUtil;
import com.caresync.erp.service.PatientAppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientAppointmentController {

    private final PatientAppointmentService patientAppointmentService;
    private final JwtUtil jwtUtil;


    @GetMapping("/appointments")
    public ResponseEntity<List<PatientAppointmentResponseDto>> getPatientAppointments(
            HttpServletRequest request) {

        Long patientId = getPatientIdFromToken(request);
        List<PatientAppointmentResponseDto> appointments = patientAppointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/appointments/book")
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @Valid @RequestBody PatientBookAppointmentRequestDto requestDto,
            HttpServletRequest request) {

        // Verify that patient is booking for themselves
        Long patientIdFromToken = getPatientIdFromToken(request);
        if (!patientIdFromToken.equals(requestDto.getPatientId())) {
            throw new IllegalArgumentException("You can only book appointments for yourself");
        }

        AppointmentResponseDto response = patientAppointmentService.bookAppointment(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/appointments/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponseDto> cancelAppointment(
            @PathVariable Long appointmentId,
            HttpServletRequest request) {

        Long patientId = getPatientIdFromToken(request);
        AppointmentResponseDto response = patientAppointmentService.cancelAppointment(appointmentId, patientId);
        return ResponseEntity.ok(response);
    }


    private Long getPatientIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Long userId = jwtUtil.extractUserId(token);
            if (userId == null) {
                throw new IllegalArgumentException("Invalid token: user ID not found");
            }
            return userId;
        }
        throw new IllegalArgumentException("Authorization header not found");
    }
}