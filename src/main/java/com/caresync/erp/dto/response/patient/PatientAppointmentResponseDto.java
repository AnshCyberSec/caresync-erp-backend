package com.caresync.erp.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "appointmentId",
        "doctorId",
        "doctorName",
        "doctorSpecialization",
        "appointmentDate",
        "appointmentTime",
        "status",
        "notes"
})
public class PatientAppointmentResponseDto {
    private Long appointmentId;
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private String notes;
}

