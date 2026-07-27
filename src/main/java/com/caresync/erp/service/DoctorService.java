package com.caresync.erp.service;

import com.caresync.erp.dto.request.doctor.DoctorRequestDto;
import com.caresync.erp.dto.response.doctor.DoctorResponseDto;
import org.springframework.data.domain.Page;

public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorRequestDto requestDto);

    DoctorResponseDto getDoctorById(Long doctorId);

    DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto requestDto);

    void deleteDoctor(Long DoctorId);

    Page<DoctorResponseDto> getAllDoctors(int page, int size, String sortBy, String direction);
}
