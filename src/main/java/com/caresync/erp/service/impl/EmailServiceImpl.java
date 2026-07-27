package com.caresync.erp.service.impl;

import com.caresync.erp.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset OTP - Hospital Management System");
        message.setText("Dear User,\n\n" +
                "You have requested to reset your password.\n\n" +
                "Your OTP for password reset is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\nHospital Management System");

        try {
            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", to);
        } catch (MailException ex) {

            log.error("Failed to send OTP email to: {}", to, ex);
            throw ex;
        }
    }
}
