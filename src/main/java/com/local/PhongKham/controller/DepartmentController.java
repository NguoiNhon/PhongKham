package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.DepartmentRequest;
import com.local.PhongKham.dto.response.DepartmentResponse;
import com.local.PhongKham.service.DepartmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public DepartmentResponse create(
            @RequestBody DepartmentRequest request
    ) {
        return departmentService.create(request);
    }

    @GetMapping
    public List<DepartmentResponse> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public DepartmentResponse getById(
            @PathVariable Long id
    ) {
        return departmentService.getById(id);
    }

    @PutMapping("/{id}")
    public DepartmentResponse update(
            @PathVariable Long id,
            @RequestBody DepartmentRequest request
    ) {
        return departmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {
        departmentService.delete(id);
    }
}