package com.caresync.erp.service;

import com.caresync.erp.dto.request.auth.ForgotPasswordRequestDto;
import com.caresync.erp.dto.request.auth.ResetPasswordRequestDto;
import com.caresync.erp.dto.request.auth.VerifyOtpRequestDto;
import com.caresync.erp.dto.response.auth.ForgotPasswordResponseDto;

public interface ForgotPasswordService {
    ForgotPasswordResponseDto sendOtp(ForgotPasswordRequestDto requestDto);

    boolean verifyOtp(VerifyOtpRequestDto requestDto);

    void resetPassword(ResetPasswordRequestDto requestDto);
}

