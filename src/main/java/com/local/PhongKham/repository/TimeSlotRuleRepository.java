package com.local.PhongKham.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.local.PhongKham.entity.Department;
import com.local.PhongKham.entity.TimeSlotRule;

import java.util.Optional;


public interface TimeSlotRuleRepository extends JpaRepository<TimeSlotRule, Long> {

        Optional<TimeSlotRule> findByDepartment_Id(Long departmentId);
        void deleteByDepartment(Department department);
}