package com.caresync.erp.dto.request.receptionist;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceptionistRegisterRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;
}