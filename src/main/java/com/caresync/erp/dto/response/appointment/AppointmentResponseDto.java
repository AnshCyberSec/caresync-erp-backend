package com.caresync.erp.dto.response.appointment;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.common.enums.BookingSource;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
@JsonPropertyOrder({
        "id",
        "doctorId",
        "doctorName",
        "patientId",
        "patientName",
        "patientPhone",
        "appointmentDate",
        "appointmentTime",
        "status",
        "bookingSource",
        "checkInTime",
        "checkOutTime",
        "active"
})
public class AppointmentResponseDto {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private String patientPhone;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    private AppointmentStatus status;
    private BookingSource bookingSource;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Boolean active;
}