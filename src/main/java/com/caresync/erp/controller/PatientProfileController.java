package com.caresync.erp.controller;

import com.caresync.erp.dto.request.patient.PatientProfileUpdateRequestDto;
import com.caresync.erp.dto.response.patient.PatientProfileResponseDto;
import com.caresync.erp.service.PatientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @GetMapping("/profile/{patientId}")
    public ResponseEntity<PatientProfileResponseDto> getPatientProfile(@PathVariable Long patientId) {
        PatientProfileResponseDto profile = patientProfileService.getPatientProfile(patientId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile/{patientId}")
    public ResponseEntity<PatientProfileResponseDto> updatePatientProfile(
            @PathVariable Long patientId,
            @Valid @RequestBody PatientProfileUpdateRequestDto requestDto) {
        PatientProfileResponseDto updatedProfile = patientProfileService.updatePatientProfile(patientId, requestDto);
        return ResponseEntity.ok(updatedProfile);
    }
}