package com.caresync.erp.service;

import com.caresync.erp.dto.response.patient.PatientDoctorResponseDto;

import java.util.List;

public interface PatientDoctorService {
    List<PatientDoctorResponseDto> getAllDoctors();
}
