package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.Role;
import com.caresync.erp.dto.request.patient.PatientLoginRequestDto;
import com.caresync.erp.dto.request.patient.PatientRegisterRequestDto;
import com.caresync.erp.dto.response.patient.PatientLoginResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.repository.UserRepository;
import com.caresync.erp.security.jwt.JwtUtil;
import com.caresync.erp.service.PatientAuthService;
import lombok.RequiredArgsConstructor;
import com.caresync.erp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PatientAuthServiceImpl implements PatientAuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void register(PatientRegisterRequestDto requestDto) {
        log.info("Patient self-registration attempt for username '{}'", requestDto.getUsername());

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("Patient registration failed — username exists: {}", requestDto.getUsername());
            throw new IllegalArgumentException("Username already exists: " + requestDto.getUsername());
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Patient registration failed — email exists: {}", requestDto.getEmail());
            throw new IllegalArgumentException("Email already exists: " + requestDto.getEmail());
        }

        if (patientRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Patient registration failed — patient email exists: {}", requestDto.getEmail());
            throw new IllegalArgumentException("Patient with this email already exists");
        }

        if (patientRepository.existsByPhone(requestDto.getPhone())) {
            log.warn("Patient registration failed — phone exists: {}", requestDto.getPhone());
            throw new IllegalArgumentException("Patient with this phone already exists");
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .role(Role.PATIENT)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        User savedUser = userRepository.save(user);

        Patient patient = Patient.builder()
                .name(requestDto.getName())
                .age(requestDto.getAge())
                .gender(requestDto.getGender())
                .phone(requestDto.getPhone())
                .email(requestDto.getEmail())
                .address(requestDto.getAddress())
                .active(true)
                .user(savedUser)
                .build();

        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient registered successfully: patientId={}, username={}",
                savedPatient.getId(), requestDto.getUsername());
    }

    @Override
    public PatientLoginResponseDto login(PatientLoginRequestDto requestDto) {
        log.info("Patient login attempt for username '{}'", requestDto.getUsername());

        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> {
                    log.warn("Patient login failed — user not found: {}", requestDto.getUsername());
                    return new ResourceNotFoundException("Invalid username or password");
                });

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            log.warn("Patient login failed — wrong password for username: {}", requestDto.getUsername());
            throw new IllegalArgumentException("Invalid username or password");
        }

        if (user.getRole() != Role.PATIENT) {
            log.warn("Patient login failed — non-patient role tried patient login: {}", requestDto.getUsername());
            throw new IllegalArgumentException("Invalid credentials. Please use patient login.");
        }

        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    log.error("Patient profile missing for userId={}", user.getId());
                    return new ResourceNotFoundException("Patient profile not found");
                });

        String token = jwtUtil.generateTokenWithUserId(
                user.getUsername(),
                user.getRole().name(),
                patient.getId(),
                "PATIENT"
        );

        log.info("Patient login successful: patientId={}, username={}", patient.getId(), user.getUsername());

        return PatientLoginResponseDto.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .patientId(patient.getId())
                .name(patient.getName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .build();
    }
}