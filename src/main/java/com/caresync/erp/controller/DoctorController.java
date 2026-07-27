package com.caresync.erp.controller;

import com.caresync.erp.dto.request.doctor.DoctorRequestDto;
import com.caresync.erp.dto.response.doctor.DoctorResponseDto;
import com.caresync.erp.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;


    @PostMapping("/create")
    public ResponseEntity<DoctorResponseDto> createDoctor(@Valid @RequestBody DoctorRequestDto requestDto){
        DoctorResponseDto responseDto =  doctorService.createDoctor(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable Long id){
        DoctorResponseDto responseDto = doctorService.getDoctorById(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorRequestDto requestDto){

        DoctorResponseDto updateDoctor =  doctorService.updateDoctor(id,requestDto);

        return ResponseEntity.ok(updateDoctor);

    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> deleteDoctor(@PathVariable Long id){
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<DoctorResponseDto>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction){

        Page<DoctorResponseDto> doctors = doctorService.getAllDoctors(page, size, sortBy, direction);
        return ResponseEntity.ok(doctors);
    }
    // PUBLIC ENDPOINT FOR DOCTOR SELF-REGISTRATION
    @PostMapping("/public-register")
    public ResponseEntity<DoctorResponseDto> publicRegisterDoctor(@Valid @RequestBody DoctorRequestDto requestDto){
        DoctorResponseDto responseDto = doctorService.createDoctor(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

}
