package com.caresync.erp.service;

import com.caresync.erp.dto.request.receptionist.CreateReceptionistRequestDto;
import com.caresync.erp.dto.request.receptionist.UpdateReceptionistRequestDto;
import com.caresync.erp.dto.response.receptionist.ReceptionistResponseDto;
import org.springframework.data.domain.Page;

public interface ReceptionistService {

    ReceptionistResponseDto createReceptionist(CreateReceptionistRequestDto requestDto);

    ReceptionistResponseDto getReceptionistById(Long receptionistId);

    ReceptionistResponseDto updateReceptionist(
            Long receptionistId,
            UpdateReceptionistRequestDto requestDto
    );

    void deleteReceptionist(Long receptionistId);

    Page<ReceptionistResponseDto> getAllReceptionists(
            int page,
            int size,
            String sortBy,
            String direction
    );
}