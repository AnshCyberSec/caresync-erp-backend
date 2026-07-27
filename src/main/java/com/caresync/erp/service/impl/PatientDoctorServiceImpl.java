package com.caresync.erp.service.impl;

import com.caresync.erp.dto.response.patient.PatientDoctorResponseDto;
import com.caresync.erp.mapper.PatientDoctorMapper;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.service.PatientDoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientDoctorServiceImpl implements PatientDoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public List<PatientDoctorResponseDto> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();

        return doctors.stream()
                .filter(Doctor::getActive)
                .map(PatientDoctorMapper::mapToResponseDto)
                .collect(Collectors.toList());
    }
}