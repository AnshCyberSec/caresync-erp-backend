package com.caresync.erp.service.impl;


import com.caresync.erp.dto.request.auth.ForgotPasswordRequestDto;
import com.caresync.erp.dto.request.auth.ResetPasswordRequestDto;
import com.caresync.erp.dto.request.auth.VerifyOtpRequestDto;
import com.caresync.erp.dto.response.auth.ForgotPasswordResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.model.User;
import com.caresync.erp.repository.UserRepository;
import com.caresync.erp.service.EmailService;
import com.caresync.erp.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import com.caresync.erp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OtpStorageService otpStorageService;

    private static final SecureRandom secureRandom = new SecureRandom();

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    @Override
    public ForgotPasswordResponseDto sendOtp(ForgotPasswordRequestDto requestDto) {
        String email = requestDto.getEmail().toLowerCase().trim();
        log.info("Forgot-password OTP requested for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("OTP request failed — no user found with email: {}", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });

        String otp = generateOtp();
        otpStorageService.storeOtp(email, otp);
        emailService.sendOtpEmail(email, otp);

        // SECURITY: OTP value ko kabhi bhi log me mat likhna — production risk hai.
        // Sirf ye log karo ki OTP kis email pe gaya, value nahi.
        log.info("OTP generated and email dispatched to: {}", email);

        return ForgotPasswordResponseDto.builder()
                .message("OTP sent successfully to your email")
                .email(email)
                .otpSent(true)
                .build();
    }

    @Override
    public boolean verifyOtp(VerifyOtpRequestDto requestDto) {
        String email = requestDto.getEmail().toLowerCase().trim();
        String otp = requestDto.getOtp();

        log.info("OTP verification attempt for email: {}", email);

        if (!otpStorageService.validateOtp(email, otp)) {
            log.warn("OTP verification failed (invalid OTP) for email: {}", email);
            throw new IllegalArgumentException("Invalid OTP. Please check and try again.");
        }

        if (otpStorageService.isOtpExpired(email)) {
            otpStorageService.removeOtp(email);
            log.warn("OTP verification failed (expired) for email: {}", email);
            throw new IllegalArgumentException("OTP has expired. Please request a new one.");
        }

        log.info("OTP verified successfully for email: {}", email);
        return true;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequestDto requestDto) {
        String email = requestDto.getEmail().toLowerCase().trim();
        String otp = requestDto.getOtp();

        log.info("Password reset attempt for email: {}", email);

        if (!otpStorageService.validateOtp(email, otp)) {
            log.warn("Password reset failed (invalid OTP) for email: {}", email);
            throw new IllegalArgumentException("Invalid OTP. Please verify again.");
        }

        if (otpStorageService.isOtpExpired(email)) {
            otpStorageService.removeOtp(email);
            log.warn("Password reset failed (expired OTP) for email: {}", email);
            throw new IllegalArgumentException("OTP has expired. Please request a new one.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Password reset failed — user not found for verified email: {}", email);
                    return new ResourceNotFoundException("User not found with email: " + email);
                });

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);

        otpStorageService.removeOtp(email);

        log.info("Password reset successfully for email: {}", email);
    }
}