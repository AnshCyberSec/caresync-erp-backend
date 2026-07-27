package com.caresync.erp.controller;

import com.caresync.erp.dto.request.receptionist.CreateReceptionistRequestDto;
import com.caresync.erp.dto.request.receptionist.UpdateReceptionistRequestDto;
import com.caresync.erp.dto.response.receptionist.ReceptionistResponseDto;
import com.caresync.erp.service.ReceptionistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    @PostMapping("/create")
    public ResponseEntity<ReceptionistResponseDto> createReceptionist(
            @Valid @RequestBody CreateReceptionistRequestDto requestDto) {

        ReceptionistResponseDto responseDto =
                receptionistService.createReceptionist(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> getReceptionistById(
            @PathVariable Long id) {

        ReceptionistResponseDto responseDto =
                receptionistService.getReceptionistById(id);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDto> updateReceptionist(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReceptionistRequestDto requestDto) {

        ReceptionistResponseDto responseDto =
                receptionistService.updateReceptionist(id, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Long id) {

        receptionistService.deleteReceptionist(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<ReceptionistResponseDto>> getAllReceptionists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Page<ReceptionistResponseDto> receptionists =
                receptionistService.getAllReceptionists(
                        page,
                        size,
                        sortBy,
                        direction
                );

        return ResponseEntity.ok(receptionists);
    }
}