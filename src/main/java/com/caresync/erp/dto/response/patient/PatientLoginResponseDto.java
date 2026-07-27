package com.caresync.erp.dto.response.patient;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatientLoginResponseDto {
    private String token;
    private String username;
    private String role;
    private Long patientId;
    private String name;
    private String email;
    private String phone;
}
