package com.caresync.erp.service;

import com.caresync.erp.dto.response.doctor.DoctorAvailableSlotDto;

import java.time.LocalDate;

public interface DoctorAvailabilityService {
    DoctorAvailableSlotDto getAvailableSlots(Long doctorId, LocalDate date);
}

