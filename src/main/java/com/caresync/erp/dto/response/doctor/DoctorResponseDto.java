package com.caresync.erp.dto.response.doctor;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "id",
        "name",
        "specialization",
        "email",
        "phone",
        "experienceYears",
        "active"
})
public class DoctorResponseDto {
    private Long id;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private Integer experienceYears;
    private Boolean active;
}
