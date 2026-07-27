package com.caresync.erp.service.impl;

import com.caresync.erp.dto.request.patient.PatientProfileUpdateRequestDto;
import com.caresync.erp.dto.response.patient.PatientProfileResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientRepository patientRepository;

    @Override
    public PatientProfileResponseDto getPatientProfile(Long patientId) {
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        return mapToProfileResponse(patient);
    }

    @Override
    @Transactional
    public PatientProfileResponseDto updatePatientProfile(Long patientId, PatientProfileUpdateRequestDto requestDto) {
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        // Update only non-null fields
        if (requestDto.getName() != null) {
            patient.setName(requestDto.getName());
        }
        if (requestDto.getAge() != null) {
            patient.setAge(requestDto.getAge());
        }
        if (requestDto.getGender() != null) {
            patient.setGender(requestDto.getGender());
        }
        if (requestDto.getPhone() != null) {
            // Check phone uniqueness
            if (patientRepository.existsByPhone(requestDto.getPhone()) &&
                    !requestDto.getPhone().equals(patient.getPhone())) {
                throw new IllegalArgumentException("Phone number already exists");
            }
            patient.setPhone(requestDto.getPhone());
        }
        if (requestDto.getEmail() != null) {
            // Check email uniqueness
            if (patientRepository.existsByEmail(requestDto.getEmail()) &&
                    !requestDto.getEmail().equals(patient.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            patient.setEmail(requestDto.getEmail());
        }
        if (requestDto.getAddress() != null) {
            patient.setAddress(requestDto.getAddress());
        }
        if (requestDto.getBloodGroup() != null) {
            patient.setBloodGroup(requestDto.getBloodGroup());
        }
        if (requestDto.getEmergencyContact() != null) {
            patient.setEmergencyContact(requestDto.getEmergencyContact());
        }
        if (requestDto.getEmergencyName() != null) {
            patient.setEmergencyName(requestDto.getEmergencyName());
        }
        if (requestDto.getMedicalHistory() != null) {
            patient.setMedicalHistory(requestDto.getMedicalHistory());
        }
        if (requestDto.getAllergies() != null) {
            patient.setAllergies(requestDto.getAllergies());
        }
        if (requestDto.getMaritalStatus() != null) {
            patient.setMaritalStatus(requestDto.getMaritalStatus());
        }
        if (requestDto.getOccupation() != null) {
            patient.setOccupation(requestDto.getOccupation());
        }
        if (requestDto.getInsuranceProvider() != null) {
            patient.setInsuranceProvider(requestDto.getInsuranceProvider());
        }
        if (requestDto.getInsuranceNumber() != null) {
            patient.setInsuranceNumber(requestDto.getInsuranceNumber());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return mapToProfileResponse(updatedPatient);
    }

    private PatientProfileResponseDto mapToProfileResponse(Patient patient) {
        return PatientProfileResponseDto.builder()
                .patientId(patient.getId())
                .name(patient.getName())
                .age(patient.getAge())
                .gender(patient.getGender())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContact(patient.getEmergencyContact())
                .emergencyName(patient.getEmergencyName())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .maritalStatus(patient.getMaritalStatus())
                .occupation(patient.getOccupation())
                .insuranceProvider(patient.getInsuranceProvider())
                .insuranceNumber(patient.getInsuranceNumber())
                .active(patient.getActive())
                .registrationDate(patient.getRegistrationDate() != null ?
                        patient.getRegistrationDate().toString() : null)
                .build();
    }
}