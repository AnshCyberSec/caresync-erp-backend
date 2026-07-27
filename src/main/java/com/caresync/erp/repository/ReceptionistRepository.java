package com.caresync.erp.repository;

import com.caresync.erp.model.Receptionist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {

    Optional<Receptionist> findByIdAndActiveTrue(Long id);

    Optional<Receptionist> findByUserId(Long userId);

    Optional<Receptionist> findByEmail(String email);

    Optional<Receptionist> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<Receptionist> findByActiveTrue();

    List<Receptionist> findByActiveFalse();


    Page<Receptionist> findByActiveTrue(Pageable pageable);
}