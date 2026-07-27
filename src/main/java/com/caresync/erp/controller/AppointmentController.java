package com.caresync.erp.controller;

import com.caresync.erp.dto.request.appointment.AppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping("/create")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody AppointmentRequestDto requestDto){
        AppointmentResponseDto responseDto =  appointmentService.createAppointments(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id){
        AppointmentResponseDto response =  appointmentService.getAppointmentById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentResponseDto>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<AppointmentResponseDto> response =
                appointmentService.getAppointmentByDoctor(
                        doctorId, page, size, sortBy, direction
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentResponseDto>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<AppointmentResponseDto> response =
                appointmentService.getAppointmentsByPatient(
                        patientId, page, size, sortBy, direction
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")

    public ResponseEntity<AppointmentResponseDto> cancelAppointment(@PathVariable long id){
        AppointmentResponseDto responseDto = appointmentService.cancelAppointments(id);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "appointmentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Page<AppointmentResponseDto> response = appointmentService.getAllAppointments(page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponseDto> completeAppointment(@PathVariable long id){
        AppointmentResponseDto responseDto = appointmentService.completeAppointment(id);
        return ResponseEntity.ok(responseDto);
    }

}
