package com.caresync.erp.controller;

import com.caresync.erp.dto.request.patient.PatientRequestDto;
import com.caresync.erp.dto.response.patient.PatientResponseDto;
import com.caresync.erp.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping("/create")
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto requestDto){
        PatientResponseDto response = patientService.createPatient(requestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Long id){
        PatientResponseDto responseDto = patientService.getPatientById(id);

        return ResponseEntity.ok(responseDto);

    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable Long id,@Valid @RequestBody PatientRequestDto requestDto){
        PatientResponseDto updatePatient = patientService.updatePatient(id,requestDto);

        return ResponseEntity.ok(updatePatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<Page<PatientResponseDto>> getAllPatients(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                                   @RequestParam(defaultValue = "asc") String direction,
                                                                   @RequestParam(required = false) String phone,
                                                                   @RequestParam(required = false) String name){

        if (phone != null || name != null) {
            Page<PatientResponseDto> results = patientService.searchPatients(phone, name, page, size);
            return ResponseEntity.ok(results);
        }

        Page<PatientResponseDto> patients = patientService.getAllPatients(page,size,sortBy,direction);
        return ResponseEntity.ok(patients);
    }

}