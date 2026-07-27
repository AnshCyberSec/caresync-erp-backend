package com.caresync.erp.mapper;

import com.caresync.erp.dto.request.receptionist.CreateReceptionistRequestDto;
import com.caresync.erp.dto.response.receptionist.ReceptionistResponseDto;
import com.caresync.erp.model.Receptionist;

public class ReceptionistMapper {

    private ReceptionistMapper() {
    }


    public static Receptionist mapToReceptionistEntity(CreateReceptionistRequestDto dto) {

        return Receptionist.builder()
                .name(dto.getName())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .active(true)
                .build();
    }


    public static ReceptionistResponseDto mapToReceptionistResponseDto(Receptionist receptionist) {

        return ReceptionistResponseDto.builder()
                .id(receptionist.getId())
                .name(receptionist.getName())
                .username(
                        receptionist.getUser() != null
                                ? receptionist.getUser().getUsername()
                                : null
                )
                .email(receptionist.getEmail())
                .phone(receptionist.getPhone())
                .address(receptionist.getAddress())
                .active(receptionist.getActive())
                .build();
    }
}