package com.caresync.erp.service.impl;

import com.caresync.erp.dto.response.doctor.DoctorAvailableSlotDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.repository.AppointmentRepository;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.service.DoctorAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    // Default time slots (30 min intervals from 9 AM to 5 PM)
    private static final List<String> DEFAULT_SLOTS = List.of(
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00"
    );

    @Override
    public DoctorAvailableSlotDto getAvailableSlots(Long doctorId, LocalDate date) {
        // Check if doctor exists
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));


        List<LocalTime> bookedSlots = appointmentRepository.findBookedSlotsByDoctorAndDate(doctorId, date);

        // Convert booked times to string format for comparison
        List<String> bookedSlotStrings = bookedSlots.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());

        // Filter available slots
        List<String> availableSlots = new ArrayList<>();
        for (String slot : DEFAULT_SLOTS) {
            if (!bookedSlotStrings.contains(slot)) {
                availableSlots.add(slot);
            }
        }

        return DoctorAvailableSlotDto.builder()
                .doctorId(doctor.getId())
                .doctorName(doctor.getName())
                .specialization(doctor.getSpecialization())
                .date(date.toString())
                .availableSlots(availableSlots)
                .build();
    }
}
