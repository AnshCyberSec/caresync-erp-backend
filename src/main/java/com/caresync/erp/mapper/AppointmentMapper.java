package com.caresync.erp.mapper;



import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.common.enums.BookingSource;
import com.caresync.erp.dto.request.appointment.AppointmentRequestDto;
import com.caresync.erp.dto.response.appointment.AppointmentResponseDto;
import com.caresync.erp.model.Appointment;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.Patient;

public class AppointmentMapper {
    private AppointmentMapper() {
    }

    public static Appointment mapToAppointmentEntity(AppointmentRequestDto dto, Doctor doctor, Patient patient) {
        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(dto.getAppointmentDate())
                .appointmentTime(dto.getAppointmentTime())
                .status(AppointmentStatus.BOOKED)
                .bookingSource(BookingSource.ONLINE)
                .active(true)
                .build();
    }


    public static Appointment mapToWalkInAppointmentEntity(
            Doctor doctor, Patient patient, java.time.LocalDate date, java.time.LocalTime time) {
        return Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(date)
                .appointmentTime(time)
                .status(AppointmentStatus.BOOKED)
                .bookingSource(BookingSource.RECEPTIONIST)
                .active(true)
                .build();
    }

    public static AppointmentResponseDto mapToAppointmentResponseDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getName())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getName())
                .patientPhone(appointment.getPatient().getPhone())
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .bookingSource(appointment.getBookingSource())
                .checkInTime(appointment.getCheckInTime())
                .checkOutTime(appointment.getCheckOutTime())
                .active(appointment.getActive())
                .build();
    }
}