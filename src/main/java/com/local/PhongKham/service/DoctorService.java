package com.local.PhongKham.service;

import com.local.PhongKham.dto.request.CreateDoctorRequest;
import com.local.PhongKham.dto.request.UpdateDoctorRequest;
import com.local.PhongKham.dto.response.DoctorResponse;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface DoctorService {

    DoctorResponse create(CreateDoctorRequest request, MultipartFile file);

    List<DoctorResponse> getAll(Long departmentId);

    DoctorResponse getById(Long id);

    DoctorResponse update(Long id, UpdateDoctorRequest request, MultipartFile file);
    
    void delete(Long id);

    List<DoctorResponse> getAvailableDoctors(
            Long departmentId,
            LocalDate date
    );
}