package com.caresync.erp.mapper;

import com.caresync.erp.dto.request.doctor.DoctorRequestDto;
import com.caresync.erp.dto.response.doctor.DoctorResponseDto;
import com.caresync.erp.model.Doctor;

public class DoctorMapper {
    private DoctorMapper(){

    }

    public static Doctor mapToDoctorEntity(DoctorRequestDto dto){
        return Doctor.builder()
                .name(dto.getName())
                .specialization(dto.getSpecialization())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .experienceYears(dto.getExperienceYears())
                .active(true)
                .build();
    }


    public static DoctorResponseDto mapToDoctorResponseDto(Doctor doctor){
        return DoctorResponseDto.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .specialization(doctor.getSpecialization())
                .phone(doctor.getPhone())
                .email(doctor.getEmail())
                .experienceYears(doctor.getExperienceYears())
                .active(doctor.getActive())
                .build();
    }
}
