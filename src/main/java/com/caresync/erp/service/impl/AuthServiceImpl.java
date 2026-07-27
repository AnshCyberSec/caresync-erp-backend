package com.caresync.erp.service.impl;

import com.caresync.erp.dto.request.auth.LoginRequestDto;
import com.caresync.erp.dto.request.auth.RegisterRequestDto;
import com.caresync.erp.dto.response.auth.LoginResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.repository.UserRepository;
import com.caresync.erp.security.jwt.JwtUtil;
import com.caresync.erp.service.AuthService;
import lombok.RequiredArgsConstructor;
import com.caresync.erp.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.caresync.erp.repository.ReceptionistRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;




    @Override
    public void register(RegisterRequestDto requestDto) {
        log.info("Registering new user with username '{}' and role '{}'",
                requestDto.getUsername(), requestDto.getRole());

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("Registration failed — username already exists: {}", requestDto.getUsername());
            throw new IllegalArgumentException("Username Already Exists: " + requestDto.getUsername());
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Registration failed — email already exists: {}", requestDto.getEmail());
            throw new IllegalArgumentException("Email already exists: " + requestDto.getEmail());
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .role(requestDto.getRole())
                .enabled(true)
                .accountNonLocked(true)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", requestDto.getUsername());
    }

    @Override
    public LoginResponseDto login(LoginRequestDto requestDto) {
        log.info("Login attempt for username '{}'", requestDto.getUsername());

        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login failed — user not found: {}", requestDto.getUsername());
                    return new ResourceNotFoundException("Invalid username or password");
                });

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            log.warn("Login failed — wrong password for username: {}", requestDto.getUsername());
            throw new IllegalArgumentException("Invalid username or password");
        }

        Long userId = null;
        String userType = null;

        if ("DOCTOR".equals(user.getRole().name())) {

            userId = doctorRepository.findByUserId(user.getId())
                    .map(doctor -> doctor.getId())
                    .orElse(null);

            userType = "DOCTOR";

        } else if ("PATIENT".equals(user.getRole().name())) {

            userId = patientRepository.findByUserId(user.getId())
                    .map(patient -> patient.getId())
                    .orElse(null);

            userType = "PATIENT";

        } else if ("RECEPTIONIST".equals(user.getRole().name())) {

            userId = receptionistRepository.findByUserId(user.getId())
                    .map(receptionist -> receptionist.getId())
                    .orElse(null);

            userType = "RECEPTIONIST";
        }

        String token = jwtUtil.generateTokenWithUserId(
                user.getUsername(),
                user.getRole().name(),
                userId,
                userType
        );

        LoginResponseDto.LoginResponseDtoBuilder builder = LoginResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .token(token);

        if ("DOCTOR".equals(user.getRole().name())) {
            doctorRepository.findByUserId(user.getId()).ifPresent(
                    doctor -> builder.doctorId(doctor.getId())
            );
        }

        if ("PATIENT".equals(user.getRole().name())) {
            patientRepository.findByUserId(user.getId()).ifPresent(
                    patient -> builder.patientId(patient.getId())
            );
        }

        if ("RECEPTIONIST".equals(user.getRole().name())) {
            receptionistRepository.findByUserId(user.getId()).ifPresent(
                    receptionist -> builder.receptionistId(receptionist.getId())
            );
        }

        log.info("Login successful for username '{}' with role '{}'", user.getUsername(), user.getRole());
        return builder.build();
    }
}