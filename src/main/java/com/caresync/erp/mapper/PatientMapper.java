package com.caresync.erp.mapper;

import com.caresync.erp.dto.request.patient.PatientRequestDto;
import com.caresync.erp.dto.response.patient.PatientResponseDto;
import com.caresync.erp.model.Patient;
import com.caresync.erp.model.User;

public class PatientMapper {

    private PatientMapper() {
    }


    public static Patient mapToPatientEntity(PatientRequestDto dto) {
        return Patient.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .active(true)
                // user will be set separately
                .build();
    }


    public static Patient mapToPatientEntityWithUser(PatientRequestDto dto, User user) {
        return Patient.builder()
                .name(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .active(true)
                .user(user)
                .build();
    }

    public static PatientResponseDto mapToPatientResponseDto(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .active(patient.getActive())
                .build();
    }
}
