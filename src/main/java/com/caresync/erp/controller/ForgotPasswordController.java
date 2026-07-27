package com.caresync.erp.controller;

import com.caresync.erp.dto.request.auth.ForgotPasswordRequestDto;
import com.caresync.erp.dto.request.auth.ResetPasswordRequestDto;
import com.caresync.erp.dto.request.auth.VerifyOtpRequestDto;
import com.caresync.erp.dto.response.auth.ForgotPasswordResponseDto;
import com.caresync.erp.service.ForgotPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    /**
     * Step 1: Send OTP to email
     * POST /api/auth/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponseDto> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDto requestDto) {
        ForgotPasswordResponseDto response = forgotPasswordService.sendOtp(requestDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Step 2: Verify OTP
     * POST /api/auth/verify-otp
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto requestDto) {
        forgotPasswordService.verifyOtp(requestDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "OTP verified successfully");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    /**
     * Step 3: Reset password
     * POST /api/auth/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestDto) {
        forgotPasswordService.resetPassword(requestDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }
}
