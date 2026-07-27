package com.caresync.erp.controller;

import com.caresync.erp.dto.request.receptionist.ReceptionistBookAppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.service.ReceptionistAppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptionist/appointments")
@RequiredArgsConstructor
public class ReceptionistAppointmentController {

    private final ReceptionistAppointmentService receptionistAppointmentService;


    @PostMapping("/book")
    public ResponseEntity<AppointmentResponseDto> bookForWalkIn(
            @Valid @RequestBody ReceptionistBookAppointmentRequestDto requestDto) {
        AppointmentResponseDto response = receptionistAppointmentService.bookForWalkInPatient(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/today")
    public ResponseEntity<List<AppointmentResponseDto>> getTodayAppointments() {
        return ResponseEntity.ok(receptionistAppointmentService.getTodayAppointments());
    }


    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(
                receptionistAppointmentService.getAllAppointments(page, size, sortBy, direction));
    }


    @PutMapping("/{id}/check-in")
    public ResponseEntity<AppointmentResponseDto> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(receptionistAppointmentService.checkIn(id));
    }


    @PutMapping("/{id}/check-out")
    public ResponseEntity<AppointmentResponseDto> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(receptionistAppointmentService.checkOut(id));
    }
}