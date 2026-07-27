package com.caresync.erp.service;

import com.caresync.erp.dto.request.patient.PatientRequestDto;
import com.caresync.erp.dto.response.patient.PatientResponseDto;
import org.springframework.data.domain.Page;

public interface PatientService {
    PatientResponseDto createPatient(PatientRequestDto requestDto);

    PatientResponseDto getPatientById(Long patientId);

    PatientResponseDto updatePatient(Long patientId,PatientRequestDto requestDto);

    void deletePatient(long patientId);

    Page<PatientResponseDto> getAllPatients(int page, int size, String sortBy, String direction);


    Page<PatientResponseDto> searchPatients(String phone, String name, int page, int size);
}