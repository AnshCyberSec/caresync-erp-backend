package com.caresync.erp.dto.response.receptionist;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReceptionistResponseDto {

    private Long id;

    private String name;

    private String username;

    private String email;

    private String phone;

    private String address;

    private Boolean active;
}