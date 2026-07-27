package com.caresync.erp.repository;

import com.caresync.erp.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByIdAndActiveTrue(Long id);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Patient> findByUserId(Long userId);


    Optional<Patient> findByPhoneAndActiveTrue(String phone);


    @Query("SELECT p FROM Patient p WHERE p.active = true " +
            "AND (:phone IS NULL OR p.phone LIKE CONCAT('%', :phone, '%')) " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Patient> searchByPhoneAndOrName(
            @Param("phone") String phone,
            @Param("name") String name,
            Pageable pageable);

    long countByActiveTrue();
    long countByRegistrationDate(LocalDate date);

    @Query("SELECT COUNT(p) FROM Patient p WHERE LOWER(p.gender) = LOWER(:gender)")
    long countByGenderIgnoreCase(@Param("gender") String gender);
}