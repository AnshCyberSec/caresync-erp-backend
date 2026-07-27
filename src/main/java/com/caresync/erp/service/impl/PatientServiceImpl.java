package com.caresync.erp.service.impl;

import com.caresync.erp.dto.request.patient.PatientRequestDto;
import com.caresync.erp.dto.response.patient.PatientResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.mapper.PatientMapper;
import com.caresync.erp.model.Patient;
import com.caresync.erp.repository.PatientRepository;
import com.caresync.erp.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponseDto createPatient(PatientRequestDto requestDto) {
        log.info("Creating patient with email: {}", requestDto.getEmail());

        if (patientRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Patient creation failed — email exists: {}", requestDto.getEmail());
            throw new IllegalArgumentException("Patient already exists with email: " + requestDto.getEmail());
        }
        if (patientRepository.existsByPhone(requestDto.getPhone())) {
            log.warn("Patient creation failed — phone exists: {}", requestDto.getPhone());
            throw new IllegalArgumentException("Patient already exists with phone: " + requestDto.getPhone());
        }

        Patient patient = PatientMapper.mapToPatientEntity(requestDto);
        Patient savedPatient = patientRepository.save(patient);
        log.info("Patient created successfully: patientId={}", savedPatient.getId());

        return PatientMapper.mapToPatientResponseDto(savedPatient);
    }

    @Override
    public PatientResponseDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> {
                    log.warn("Patient not found: patientId={}", patientId);
                    return new ResourceNotFoundException("Patient not found with id: " + patientId);
                });
        return PatientMapper.mapToPatientResponseDto(patient);
    }

    @Override
    public PatientResponseDto updatePatient(Long patientId, PatientRequestDto requestDto) {
        log.info("Updating patient: patientId={}", patientId);

        Patient existingPatient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> {
                    log.warn("Patient update failed — not found: patientId={}", patientId);
                    return new ResourceNotFoundException("Patient not found with id: " + patientId);
                });

        if (requestDto.getName() != null) {
            existingPatient.setName(requestDto.getName());
        }
        if (requestDto.getAge() != null) {
            existingPatient.setAge(requestDto.getAge());
        }
        if (requestDto.getGender() != null) {
            existingPatient.setGender(requestDto.getGender());
        }
        if (requestDto.getAddress() != null) {
            existingPatient.setAddress(requestDto.getAddress());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(existingPatient.getEmail())) {
            if (patientRepository.existsByEmail(requestDto.getEmail())) {
                log.warn("Patient update failed — email exists: {}", requestDto.getEmail());
                throw new IllegalArgumentException("Patient already exists with email: " + requestDto.getEmail());
            }
            existingPatient.setEmail(requestDto.getEmail());
        }

        if (requestDto.getPhone() != null && !requestDto.getPhone().equals(existingPatient.getPhone())) {
            if (patientRepository.existsByPhone(requestDto.getPhone())) {
                log.warn("Patient update failed — phone exists: {}", requestDto.getPhone());
                throw new IllegalArgumentException("Patient already exists with phone: " + requestDto.getPhone());
            }
            existingPatient.setPhone(requestDto.getPhone());
        }

        Patient updatedPatient = patientRepository.save(existingPatient);
        log.info("Patient updated successfully: patientId={}", patientId);

        return PatientMapper.mapToPatientResponseDto(updatedPatient);
    }

    @Override
    public void deletePatient(long patientId) {
        log.info("Deleting (soft) patient: patientId={}", patientId);

        Patient patient = patientRepository.findByIdAndActiveTrue(patientId)
                .orElseThrow(() -> {
                    log.warn("Patient delete failed — not found: patientId={}", patientId);
                    return new ResourceNotFoundException("Patient not found with id: " + patientId);
                });
        patient.setActive(false);
        patientRepository.save(patient);
        log.info("Patient deactivated successfully: patientId={}", patientId);
    }

    @Override
    public Page<PatientResponseDto> getAllPatients(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        return patientPage.map(PatientMapper::mapToPatientResponseDto);
    }

    @Override
    public Page<PatientResponseDto> searchPatients(String phone, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Patient> patientPage = patientRepository.searchByPhoneAndOrName(phone, name, pageable);
        return patientPage.map(PatientMapper::mapToPatientResponseDto);
    }
}