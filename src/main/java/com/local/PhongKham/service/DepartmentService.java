package com.local.PhongKham.service;

import com.local.PhongKham.dto.request.DepartmentRequest;
import com.local.PhongKham.dto.response.DepartmentResponse;
import java.util.List;


public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest request);

    List<DepartmentResponse> getAll();

    DepartmentResponse getById(Long id);

    DepartmentResponse update(Long id, DepartmentRequest request);

    void delete(Long id);
}