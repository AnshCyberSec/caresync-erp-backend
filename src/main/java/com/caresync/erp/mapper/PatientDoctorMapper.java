package com.caresync.erp.mapper;

import com.caresync.erp.dto.response.patient.PatientDoctorResponseDto;
import com.caresync.erp.model.Doctor;

public class PatientDoctorMapper {

    private PatientDoctorMapper() {

    }

    public static PatientDoctorResponseDto mapToResponseDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        return PatientDoctorResponseDto.builder()
                .doctorId(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .active(doctor.getActive())
                .build();
    }
}