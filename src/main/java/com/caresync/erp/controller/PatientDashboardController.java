package com.caresync.erp.controller;

import com.caresync.erp.dto.response.patient.PatientDashboardDto;
import com.caresync.erp.service.getPatientDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientDashboardController {
    private final getPatientDashboard patientDashboardService;


    @GetMapping("/dashboard/{patientId}")
    public ResponseEntity<PatientDashboardDto> getPatientDashboard(@PathVariable Long patientId) {
        PatientDashboardDto dashboard = patientDashboardService.getPatientDashboard(patientId);
        return ResponseEntity.ok(dashboard);
    }
}
