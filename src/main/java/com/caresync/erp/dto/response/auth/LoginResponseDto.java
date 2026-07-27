package com.caresync.erp.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseDto {
    private String token;
    private String username;
    private String role;
    private Long doctorId;
    private Long patientId;
    private Long receptionistId;
}