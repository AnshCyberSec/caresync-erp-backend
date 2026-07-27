package com.caresync.erp.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Slf4j
@Component
public class OtpStorageService {

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    private String normalizeEmail(String email) {
        return email != null ? email.toLowerCase().trim() : null;
    }

    public void storeOtp(String email, String otp) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail != null) {
            otpStorage.put(normalizedEmail, new OtpData(otp, System.currentTimeMillis()));

            log.debug("OTP stored for email: {}", normalizedEmail);
        }
    }

    public boolean validateOtp(String email, String otp) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            return false;
        }

        OtpData otpData = otpStorage.get(normalizedEmail);
        if (otpData == null) {
            log.debug("No OTP found for email: {}", normalizedEmail);
            return false;
        }

        boolean isValid = otpData.getOtp().equals(otp);
        log.debug("OTP validation for email: {} | valid={}", normalizedEmail, isValid);
        return isValid;
    }

    public void removeOtp(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail != null) {
            otpStorage.remove(normalizedEmail);
            log.debug("OTP removed for email: {}", normalizedEmail);
        }
    }

    public boolean isOtpExpired(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (normalizedEmail == null) {
            return true;
        }

        OtpData otpData = otpStorage.get(normalizedEmail);
        if (otpData == null) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        long expiryTime = otpData.getTimestamp() + (5 * 60 * 1000);
        return currentTime > expiryTime;
    }

    private static class OtpData {
        private final String otp;
        private final long timestamp;

        public OtpData(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }

        public String getOtp() { return otp; }
        public long getTimestamp() { return timestamp; }
    }
}