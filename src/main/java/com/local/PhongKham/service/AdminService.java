package com.local.PhongKham.service;

import com.local.PhongKham.dto.request.AdminRequest;
import com.local.PhongKham.dto.response.AdminResponse;

import java.util.List;

public interface AdminService {

    AdminResponse create(AdminRequest request);

    List<AdminResponse> getAll();

    AdminResponse getById(Long id);

    AdminResponse update(
            Long id,
            AdminRequest request
    );
}