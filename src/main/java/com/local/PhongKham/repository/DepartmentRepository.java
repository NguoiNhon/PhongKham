package com.local.PhongKham.repository;

import com.local.PhongKham.entity.Department;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
                 boolean existsByName(String name);
}