package com.caresync.erp.dto.response.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ForgotPasswordResponseDto {
    private String message;
    private String email;
    private boolean otpSent;
}

