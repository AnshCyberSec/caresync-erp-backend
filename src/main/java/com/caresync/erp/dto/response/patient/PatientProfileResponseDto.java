package com.caresync.erp.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "patientId",
        "name",
        "age",
        "gender",
        "email",
        "phone",
        "address",
        "bloodGroup",
        "emergencyContact",
        "emergencyName",
        "medicalHistory",
        "allergies",
        "maritalStatus",
        "occupation",
        "insuranceProvider",
        "insuranceNumber",
        "active",
        "registrationDate"
})
public class PatientProfileResponseDto {
    private Long patientId;
    private String name;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private String emergencyName;
    private String medicalHistory;
    private String allergies;
    private String maritalStatus;
    private String occupation;
    private String insuranceProvider;
    private String insuranceNumber;
    private Boolean active;
    private String registrationDate;
}