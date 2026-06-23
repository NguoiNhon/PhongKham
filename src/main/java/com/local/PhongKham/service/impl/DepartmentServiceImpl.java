package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.request.DepartmentRequest;
import com.local.PhongKham.dto.response.DepartmentResponse;
import com.local.PhongKham.entity.Department;
import com.local.PhongKham.repository.DepartmentRepository;
import com.local.PhongKham.repository.TimeSlotRuleRepository;
import com.local.PhongKham.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl
        implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final TimeSlotRuleRepository timeSlotRuleRepository;

    @Override
    public DepartmentResponse create(DepartmentRequest request) {

        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Department đã tồn tại");
        }

        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return mapToResponse(
                departmentRepository.save(department)
        );
    }

    @Override
    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public DepartmentResponse getById(Long id) {

        Department department =
                departmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy department")
                        );

        return mapToResponse(department);
    }

    @Override
    public DepartmentResponse update(Long id, DepartmentRequest request) {

        Department department =
                departmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy department")
                        );

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        return mapToResponse(
                departmentRepository.save(department)
        );
    }

   @Override
   @Transactional
        public void delete(Long id) {

        Department department =
                departmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy department"
                                ));

        // xóa time slot rule trước
        timeSlotRuleRepository
                .deleteByDepartment(department);

        // xóa department
        departmentRepository.delete(department);
        }

    // mapper
    private DepartmentResponse mapToResponse(Department d) {
        return DepartmentResponse.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .build();
    }
}
