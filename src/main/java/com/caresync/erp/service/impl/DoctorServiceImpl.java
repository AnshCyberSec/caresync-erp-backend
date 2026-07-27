package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.Role;
import com.caresync.erp.dto.request.doctor.DoctorRequestDto;
import com.caresync.erp.dto.response.doctor.DoctorResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.mapper.DoctorMapper;
import com.caresync.erp.model.Doctor;
import com.caresync.erp.model.User;
import com.caresync.erp.repository.DoctorRepository;
import com.caresync.erp.repository.UserRepository;
import com.caresync.erp.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DoctorResponseDto createDoctor(DoctorRequestDto requestDto) {
        log.info("Creating doctor with email: {}", requestDto.getEmail());

        if (doctorRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Doctor creation failed — email exists: {}", requestDto.getEmail());
            throw new IllegalArgumentException("Doctor already exists with email: " + requestDto.getEmail());
        }

        if (doctorRepository.existsByPhone(requestDto.getPhone())) {
            log.warn("Doctor creation failed — phone exists: {}", requestDto.getPhone());
            throw new IllegalArgumentException("Doctor already exists with phone: " + requestDto.getPhone());
        }

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("Doctor creation failed — username exists: {}", requestDto.getUsername());
            throw new IllegalArgumentException("Username already exists: " + requestDto.getUsername());
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .role(Role.DOCTOR)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        User savedUser = userRepository.save(user);

        Doctor doctor = DoctorMapper.mapToDoctorEntity(requestDto);
        doctor.setUser(savedUser);

        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor created successfully: doctorId={}, email={}", savedDoctor.getId(), requestDto.getEmail());

        return DoctorMapper.mapToDoctorResponseDto(savedDoctor);
    }

    @Override
    public DoctorResponseDto getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findByIdAndActiveTrue(doctorId)
                .orElseThrow(() -> {
                    log.warn("Doctor not found: doctorId={}", doctorId);
                    return new ResourceNotFoundException("Doctor not found with id: " + doctorId);
                });
        return DoctorMapper.mapToDoctorResponseDto(doctor);
    }

    @Override
    public DoctorResponseDto updateDoctor(Long doctorId, DoctorRequestDto requestDto) {
        log.info("Updating doctor: doctorId={}", doctorId);

        Doctor existingDoctor = doctorRepository.findByIdAndActiveTrue(doctorId)
                .orElseThrow(() -> {
                    log.warn("Doctor update failed — not found: doctorId={}", doctorId);
                    return new ResourceNotFoundException("Doctor not found with id: " + doctorId);
                });

        if (requestDto.getName() != null) {
            existingDoctor.setName(requestDto.getName());
        }

        if (requestDto.getSpecialization() != null) {
            existingDoctor.setSpecialization(requestDto.getSpecialization());
        }

        if (requestDto.getExperienceYears() != null) {
            existingDoctor.setExperienceYears(requestDto.getExperienceYears());
        }

        if (requestDto.getEmail() != null && !requestDto.getEmail().equals(existingDoctor.getEmail())) {
            if (doctorRepository.existsByEmail(requestDto.getEmail())) {
                log.warn("Doctor update failed — email exists: {}", requestDto.getEmail());
                throw new IllegalArgumentException("Doctor exists with email: " + requestDto.getEmail());
            }
            existingDoctor.setEmail(requestDto.getEmail());
            if (existingDoctor.getUser() != null) {
                existingDoctor.getUser().setEmail(requestDto.getEmail());
                userRepository.save(existingDoctor.getUser());
            }
        }

        if (requestDto.getPhone() != null && !requestDto.getPhone().equals(existingDoctor.getPhone())) {
            if (doctorRepository.existsByPhone(requestDto.getPhone())) {
                log.warn("Doctor update failed — phone exists: {}", requestDto.getPhone());
                throw new IllegalArgumentException("Doctor already exists with phone: " + requestDto.getPhone());
            }
            existingDoctor.setPhone(requestDto.getPhone());
        }

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        log.info("Doctor updated successfully: doctorId={}", doctorId);

        return DoctorMapper.mapToDoctorResponseDto(updatedDoctor);
    }

    @Override
    public void deleteDoctor(Long doctorId) {
        log.info("Deleting (soft) doctor: doctorId={}", doctorId);

        Doctor doctor = doctorRepository.findByIdAndActiveTrue(doctorId)
                .orElseThrow(() -> {
                    log.warn("Doctor delete failed — not found: doctorId={}", doctorId);
                    return new ResourceNotFoundException("Doctor not found with id: " + doctorId);
                });

        doctor.setActive(false);
        doctorRepository.save(doctor);
        log.info("Doctor deactivated successfully: doctorId={}", doctorId);
    }

    @Override
    public Page<DoctorResponseDto> getAllDoctors(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        return doctorPage.map(DoctorMapper::mapToDoctorResponseDto);
    }
}
