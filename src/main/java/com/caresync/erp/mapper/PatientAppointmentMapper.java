package com.caresync.erp.mapper;

import com.caresync.erp.dto.response.patient.PatientAppointmentResponseDto;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;

public class PatientAppointmentMapper {
    private PatientAppointmentMapper() {

    }

    public static PatientAppointmentResponseDto mapToResponseDto(Appointment appointment, Doctor doctor) {
        if (appointment == null) {
            return null;
        }

        return PatientAppointmentResponseDto.builder()
                .appointmentId(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(doctor != null ? doctor.getName() : "Unknown Doctor")
                .doctorSpecialization(doctor != null ? doctor.getSpecialization() : "N/A")
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus().name())
                .build();
    }
}

