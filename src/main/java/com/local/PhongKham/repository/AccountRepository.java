package com.local.PhongKham.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.local.PhongKham.entity.Account;
import com.local.PhongKham.entity.EmailVerificationOtp;

import java.util.Optional;
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
