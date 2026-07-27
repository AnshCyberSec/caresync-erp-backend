package com.caresync.erp.controller;

import com.caresync.erp.common.enums.Role;
import com.caresync.erp.dto.request.auth.LoginRequestDto;
import com.caresync.erp.dto.request.auth.RegisterRequestDto;
import com.caresync.erp.dto.request.receptionist.ReceptionistRegisterRequestDto;
import com.caresync.erp.dto.response.auth.LoginResponseDto;
import com.caresync.erp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid
            @RequestBody RegisterRequestDto requestDto) {

        authService.register(requestDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // LOGIN USER
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid
            @RequestBody LoginRequestDto requestDto) {

        LoginResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/public-register/receptionist")
    public ResponseEntity<String> publicRegisterReceptionist(
            @Valid @RequestBody ReceptionistRegisterRequestDto request) {

        RegisterRequestDto requestDto = new RegisterRequestDto();

        requestDto.setUsername(request.getUsername());
        requestDto.setPassword(request.getPassword());
        requestDto.setEmail(request.getEmail());
        requestDto.setRole(Role.RECEPTIONIST);

        authService.register(requestDto);

        return new ResponseEntity<>("Receptionist registered successfully", HttpStatus.CREATED);
    }
}
