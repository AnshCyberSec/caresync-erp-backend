package com.caresync.erp.service;

import com.caresync.erp.dto.request.patient.PatientProfileUpdateRequestDto;
import com.caresync.erp.dto.response.patient.PatientProfileResponseDto;

public interface PatientProfileService {
    PatientProfileResponseDto getPatientProfile(Long patientId);
    PatientProfileResponseDto updatePatientProfile(Long patientId, PatientProfileUpdateRequestDto requestDto);
}

