package com.caresync.erp.repository;

import com.caresync.erp.common.enums.AppointmentStatus;
import com.caresync.erp.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Optional<Appointment> findByIdAndActiveTrue(Long id);

    boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndActiveTrue(
            Long doctor_id, LocalDate appointmentDate, LocalTime appointmentTime);

    Page<Appointment> findByDoctor_IdAndActiveTrue(Long doctorId, Pageable pageable);

    Page<Appointment> findByPatient_IdAndActiveTrue(Long patientId, Pageable pageable);

    @Query("SELECT a.appointmentTime FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.appointmentDate = :date AND a.active = true " +
            "AND a.status != 'CANCELLED'")
    List<LocalTime> findBookedSlotsByDoctorAndDate(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date);

    // ================= DASHBOARD (ADMIN) =================
    long countByActiveTrue();
    long countByAppointmentDateAndActiveTrue(LocalDate date);
    long countByStatusAndActiveTrue(AppointmentStatus status);

    // ================= DASHBOARD (DOCTOR) =================
    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId AND a.active = true")
    long countDistinctPatientsByDoctorId(@Param("doctorId") Long doctorId);

    long countByDoctor_IdAndActiveTrue(Long doctorId);
    long countByDoctor_IdAndAppointmentDateAndActiveTrue(Long doctorId, LocalDate date);
    long countByDoctor_IdAndAppointmentDateAndStatusAndActiveTrue(
            Long doctorId, LocalDate date, AppointmentStatus status);
    long countByDoctor_IdAndAppointmentDateAfterAndStatusAndActiveTrue(
            Long doctorId, LocalDate date, AppointmentStatus status);
    long countByDoctor_IdAndStatusAndActiveTrue(Long doctorId, AppointmentStatus status);



    List<Appointment> findByAppointmentDateAndActiveTrueOrderByAppointmentTimeAsc(LocalDate date);


    long countByAppointmentDateAndStatusAndActiveTrue(LocalDate date, AppointmentStatus status);


    Page<Appointment> findByActiveTrue(Pageable pageable);

    // ================= REPORTS =================
    List<Appointment> findByAppointmentDate(LocalDate date);

    long countByAppointmentDateBetweenAndStatus(LocalDate start, LocalDate end, AppointmentStatus status);

    @Query(value = "SELECT DAY(a.appointment_date) as day, COUNT(*) as cnt " +
            "FROM appointments a " +
            "WHERE a.appointment_date BETWEEN :start AND :end " +
            "GROUP BY DAY(a.appointment_date) " +
            "ORDER BY day", nativeQuery = true)
    List<Object[]> findDailySummaryRaw(@Param("start") LocalDate start, @Param("end") LocalDate end);
}