package com.caresync.erp.repository;

import com.caresync.erp.model.Doctor;
import com.caresync.erp.repository.projection.DoctorPerformanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByIdAndActiveTrue(Long id);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<Doctor> findByUserId(Long userId);

    long countByActiveTrue();

    @Query("SELECT AVG(d.experienceYears) FROM Doctor d")
    Double findAverageExperienceYears();


    @Query("SELECT new com.caresync.erp.repository.projection.DoctorPerformanceProjection(" +
            "d.id, d.name, d.specialization, " +
            "COUNT(a), " +
            "SUM(CASE WHEN a.status = com.caresync.erp.common.enums.AppointmentStatus.COMPLETED THEN 1L ELSE 0L END), " +
            "SUM(CASE WHEN a.status = com.caresync.erp.common.enums.AppointmentStatus.CANCELLED THEN 1L ELSE 0L END)) " +
            "FROM Doctor d LEFT JOIN d.appointments a " +
            "GROUP BY d.id, d.name, d.specialization")
    List<DoctorPerformanceProjection> findDoctorPerformance();
}