package com.local.PhongKham.repository;

import com.local.PhongKham.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    Optional<Patient> findByAccountId(Long accountId);
}