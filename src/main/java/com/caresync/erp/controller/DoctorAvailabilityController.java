package com.caresync.erp.controller;

import com.caresync.erp.dto.response.doctor.DoctorAvailableSlotDto;
import com.caresync.erp.service.DoctorAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;


    @GetMapping("/{doctorId}/available-slots")
    public ResponseEntity<DoctorAvailableSlotDto> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DoctorAvailableSlotDto slots = doctorAvailabilityService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok(slots);
    }
}
