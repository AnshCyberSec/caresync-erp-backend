package com.caresync.erp.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "doctorId",
        "name",
        "specialization",
        "experienceYears",
        "email",
        "phone",
        "active"
})
public class PatientDoctorResponseDto {
    private Long doctorId;
    private String name;
    private String specialization;
    private Integer experienceYears;
    private String email;
    private String phone;
    private Boolean active;
}

