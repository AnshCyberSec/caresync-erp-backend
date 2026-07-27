package com.caresync.erp.service.impl;

import com.caresync.erp.common.enums.Role;
import com.caresync.erp.dto.request.receptionist.CreateReceptionistRequestDto;
import com.caresync.erp.dto.request.receptionist.UpdateReceptionistRequestDto;
import com.caresync.erp.dto.response.receptionist.ReceptionistResponseDto;
import com.caresync.erp.exception.ResourceNotFoundException;
import com.caresync.erp.mapper.ReceptionistMapper;
import com.caresync.erp.model.Receptionist;
import com.caresync.erp.model.User;
import com.caresync.erp.repository.ReceptionistRepository;
import com.caresync.erp.repository.UserRepository;
import com.caresync.erp.service.ReceptionistService;
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
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository receptionistRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ReceptionistResponseDto createReceptionist(CreateReceptionistRequestDto requestDto) {

        log.info("Creating receptionist with email: {}", requestDto.getEmail());

        if (receptionistRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Receptionist creation failed — email already exists: {}", requestDto.getEmail());
            throw new IllegalArgumentException(
                    "Receptionist already exists with email: " + requestDto.getEmail()
            );
        }

        if (receptionistRepository.existsByPhone(requestDto.getPhone())) {
            log.warn("Receptionist creation failed — phone already exists: {}", requestDto.getPhone());
            throw new IllegalArgumentException(
                    "Receptionist already exists with phone: " + requestDto.getPhone()
            );
        }

        if (userRepository.existsByUsername(requestDto.getUsername())) {
            log.warn("Receptionist creation failed — username already exists: {}", requestDto.getUsername());
            throw new IllegalArgumentException(
                    "Username already exists: " + requestDto.getUsername()
            );
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Receptionist creation failed — email already exists in users table: {}", requestDto.getEmail());
            throw new IllegalArgumentException(
                    "Email already exists: " + requestDto.getEmail()
            );
        }

        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .role(Role.RECEPTIONIST)
                .enabled(true)
                .accountNonLocked(true)
                .build();

        User savedUser = userRepository.save(user);

        Receptionist receptionist =
                ReceptionistMapper.mapToReceptionistEntity(requestDto);

        receptionist.setUser(savedUser);

        Receptionist savedReceptionist =
                receptionistRepository.save(receptionist);

        log.info(
                "Receptionist created successfully. receptionistId={}, username={}",
                savedReceptionist.getId(),
                savedUser.getUsername()
        );

        return ReceptionistMapper
                .mapToReceptionistResponseDto(savedReceptionist);
    }

    @Override
    public ReceptionistResponseDto getReceptionistById(Long receptionistId) {

        Receptionist receptionist = receptionistRepository
                .findByIdAndActiveTrue(receptionistId)
                .orElseThrow(() -> {
                    log.warn("Receptionist not found: receptionistId={}", receptionistId);
                    return new ResourceNotFoundException(
                            "Receptionist not found with id: " + receptionistId
                    );
                });

        return ReceptionistMapper
                .mapToReceptionistResponseDto(receptionist);
    }

    @Override
    @Transactional
    public ReceptionistResponseDto updateReceptionist(
            Long receptionistId,
            UpdateReceptionistRequestDto requestDto) {

        log.info("Updating receptionist: receptionistId={}", receptionistId);

        Receptionist receptionist = receptionistRepository
                .findByIdAndActiveTrue(receptionistId)
                .orElseThrow(() -> {
                    log.warn("Receptionist update failed — not found: receptionistId={}", receptionistId);
                    return new ResourceNotFoundException(
                            "Receptionist not found with id: " + receptionistId
                    );
                });

        // Update Name
        if (requestDto.getName() != null) {
            receptionist.setName(requestDto.getName());
        }

        // Update Address
        if (requestDto.getAddress() != null) {
            receptionist.setAddress(requestDto.getAddress());
        }

        // Update Email
        if (requestDto.getEmail() != null
                && !requestDto.getEmail().equals(receptionist.getEmail())) {

            if (receptionistRepository.existsByEmail(requestDto.getEmail())
                    || userRepository.existsByEmail(requestDto.getEmail())) {

                throw new IllegalArgumentException(
                        "Email already exists: " + requestDto.getEmail()
                );
            }

            receptionist.setEmail(requestDto.getEmail());

            if (receptionist.getUser() != null) {
                receptionist.getUser().setEmail(requestDto.getEmail());
                userRepository.save(receptionist.getUser());
            }
        }

        // Update Phone
        if (requestDto.getPhone() != null
                && !requestDto.getPhone().equals(receptionist.getPhone())) {

            if (receptionistRepository.existsByPhone(requestDto.getPhone())) {
                throw new IllegalArgumentException(
                        "Receptionist already exists with phone: " + requestDto.getPhone()
                );
            }

            receptionist.setPhone(requestDto.getPhone());
        }

        Receptionist updatedReceptionist = receptionistRepository.save(receptionist);

        log.info("Receptionist updated successfully: receptionistId={}", receptionistId);

        return ReceptionistMapper.mapToReceptionistResponseDto(updatedReceptionist);
    }

    @Override
    @Transactional
    public void deleteReceptionist(Long receptionistId) {

        log.info("Soft deleting receptionist: receptionistId={}",
                receptionistId);

        Receptionist receptionist = receptionistRepository
                .findByIdAndActiveTrue(receptionistId)
                .orElseThrow(() -> {
                    log.warn("Receptionist delete failed — not found: receptionistId={}",
                            receptionistId);

                    return new ResourceNotFoundException(
                            "Receptionist not found with id: "
                                    + receptionistId
                    );
                });

        receptionist.setActive(false);

        receptionistRepository.save(receptionist);

        log.info("Receptionist deactivated successfully: receptionistId={}",
                receptionistId);
    }



    @Override
    public Page<ReceptionistResponseDto> getAllReceptionists(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Receptionist> receptionistPage =
                receptionistRepository.findByActiveTrue(pageable);

        return receptionistPage.map(
                ReceptionistMapper::mapToReceptionistResponseDto
        );
    }
}