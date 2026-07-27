package com.caresync.erp.dto.response.doctor;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DoctorAvailableSlotDto {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String date;
    private List<String> availableSlots;
}