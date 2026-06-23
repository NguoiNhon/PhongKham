package com.local.PhongKham.repository;

import com.local.PhongKham.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp>
    findTopByEmailOrderByIdDesc(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);

}