package com.caresync.erp.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonPropertyOrder({
        "id",
        "name",
        "age",
        "gender",
        "email",
        "phone",
        "address",
        "active"
})
public class PatientResponseDto {
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private Boolean active;
}