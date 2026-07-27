package com.caresync.erp.dto.request.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientProfileUpdateRequestDto {
    private String name;

    @Min(value = 0, message = "Age cannot be negative")
    @Max(value = 120, message = "Invalid age")
    private Integer age;

    private String gender;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @Email(message = "Invalid email format")
    private String email;

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
}