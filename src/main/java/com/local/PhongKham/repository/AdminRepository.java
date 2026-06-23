package com.local.PhongKham.repository;

import com.local.PhongKham.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository
        extends JpaRepository<Admin, Long> {
}