package com.caresync.erp.dto.request.receptionist;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReceptionistBookAppointmentRequestDto {


    private Long patientId;


    private String patientName;
    private Integer patientAge;
    private String patientGender;

    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String patientPhone;

    private String patientEmail;
    private String patientAddress;


    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in past")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;
}