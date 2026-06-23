package com.local.PhongKham.repository;

import com.local.PhongKham.entity.EmailVerificationOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface EmailVerificationOtpRepository
        extends JpaRepository<EmailVerificationOtp, Long> {

    Optional<EmailVerificationOtp> findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);
}