package com.local.PhongKham.repository;

import com.local.PhongKham.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByDepartment_Id(Long departmentId);

}