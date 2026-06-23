package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.AdminRequest;
import com.local.PhongKham.dto.response.AdminResponse;
import com.local.PhongKham.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping
    public AdminResponse create(
            @RequestBody AdminRequest request
    ) {

        return adminService.create(request);
    }

    @GetMapping
    public List<AdminResponse> getAll() {

        return adminService.getAll();
    }

    @GetMapping("/{id}")
    public AdminResponse getById(
            @PathVariable Long id
    ) {

        return adminService.getById(id);
    }

    @PutMapping("/{id}")
    public AdminResponse update(
            @PathVariable Long id,
            @RequestBody AdminRequest request
    ) {

        return adminService.update(id, request);
    }
}