package com.caresync.erp.controller;

import com.caresync.erp.dto.request.patient.PatientLoginRequestDto;
import com.caresync.erp.dto.request.patient.PatientRegisterRequestDto;
import com.caresync.erp.dto.response.patient.PatientLoginResponseDto;
import com.caresync.erp.service.PatientAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientAuthController {
    private final PatientAuthService patientAuthService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody PatientRegisterRequestDto requestDto){
        patientAuthService.register(requestDto);
        return new ResponseEntity<>("Patient register successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<PatientLoginResponseDto> login(@Valid @RequestBody PatientLoginRequestDto requestDto){
        PatientLoginResponseDto response = patientAuthService.login(requestDto);
        return ResponseEntity.ok(response);
    }
}

