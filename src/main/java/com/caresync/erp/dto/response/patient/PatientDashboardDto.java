package com.caresync.erp.dto.response.patient;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "patientId",
        "name",
        "email",
        "phone",
        "age",
        "gender",
        "address",
        "active",
        "totalAppointments",
        "upcomingAppointments",
        "completedAppointments",
        "cancelledAppointments",
        "recentAppointments"
})
public class PatientDashboardDto {
    private Long patientId;
    private String name;
    private String email;
    private String phone;
    private Integer age;
    private String gender;
    private String address;
    private Boolean active;

    // Statistics
    private Integer totalAppointments;
    private Integer upcomingAppointments;
    private Integer completedAppointments;
    private Integer cancelledAppointments;

    // Recent appointments
    private List<PatientAppointmentDto> recentAppointments;

    @Getter
    @Setter
    @Builder
    public static class PatientAppointmentDto {
        private Long appointmentId;
        private Long doctorId;
        private String doctorName;
        private String doctorSpecialization;
        private LocalDate appointmentDate;
        private String appointmentTime;
        private String status;
        private String notes;
    }
}
