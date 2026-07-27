package com.caresync.erp.controller;

import com.caresync.erp.dto.response.patient.PatientDoctorResponseDto;
import com.caresync.erp.service.PatientDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientDoctorController {

    private final PatientDoctorService patientDoctorService;

    /**
     * Get all doctors for patient booking
     * GET /api/patient/doctors
     */
    @GetMapping("/doctors")
    public ResponseEntity<List<PatientDoctorResponseDto>> getAllDoctors() {
        List<PatientDoctorResponseDto> doctors = patientDoctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}