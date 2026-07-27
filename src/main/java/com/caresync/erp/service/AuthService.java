package com.caresync.erp.service;

import com.caresync.erp.dto.request.auth.LoginRequestDto;
import com.caresync.erp.dto.request.auth.RegisterRequestDto;
import com.caresync.erp.dto.response.auth.LoginResponseDto;

public interface AuthService {
    void register(RegisterRequestDto requestDto);

    LoginResponseDto login(LoginRequestDto requestDto);


}
