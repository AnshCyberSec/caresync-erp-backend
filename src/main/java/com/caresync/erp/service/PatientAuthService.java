package com.caresync.erp.service;

import com.caresync.erp.dto.request.patient.PatientLoginRequestDto;
import com.caresync.erp.dto.request.patient.PatientRegisterRequestDto;
import com.caresync.erp.dto.response.patient.PatientLoginResponseDto;

public interface PatientAuthService {
    void register(PatientRegisterRequestDto requestDto);

    PatientLoginResponseDto login(PatientLoginRequestDto requestDto);
}

