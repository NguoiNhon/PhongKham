package com.local.PhongKham.service;

import com.local.PhongKham.dto.request.TimeSlotRuleRequest;
import com.local.PhongKham.dto.response.TimeSlotRuleResponse;

import java.util.List;

public interface TimeSlotRuleService {

    TimeSlotRuleResponse create(
            TimeSlotRuleRequest request
    );

    TimeSlotRuleResponse update(
            Long id,
            TimeSlotRuleRequest request
    );

    void delete(Long id);

    TimeSlotRuleResponse getById(Long id);

    List<TimeSlotRuleResponse> getAll();

    TimeSlotRuleResponse getByDepartmentId(Long departmentId);
}