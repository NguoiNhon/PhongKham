package com.local.PhongKham.service;

import com.local.PhongKham.dto.response.PatientResponse;
import com.local.PhongKham.dto.request.PatientCreateRequest;
import com.local.PhongKham.dto.request.UpdatePatientRequest;

import java.util.List;

public interface PatientService {

    PatientResponse getMyProfile(
            String username
    );
    PatientResponse updateMyProfile(
            String username,
            UpdatePatientRequest request
    );
    PatientResponse create(
            PatientCreateRequest request
    );
    List<PatientResponse> getAllPatients();

    PatientResponse update(Long id, UpdatePatientRequest request);
}