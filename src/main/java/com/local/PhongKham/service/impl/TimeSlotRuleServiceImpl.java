package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.request.TimeSlotRuleRequest;
import com.local.PhongKham.dto.response.TimeSlotRuleResponse;
import com.local.PhongKham.entity.Department;
import com.local.PhongKham.entity.TimeSlotRule;
import com.local.PhongKham.repository.DepartmentRepository;
import com.local.PhongKham.repository.TimeSlotRuleRepository;
import com.local.PhongKham.service.TimeSlotRuleService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotRuleServiceImpl
        implements TimeSlotRuleService {

    private final TimeSlotRuleRepository
            timeSlotRuleRepository;

    private final DepartmentRepository
            departmentRepository;

    @Override
    public TimeSlotRuleResponse create(
            TimeSlotRuleRequest request
    ) {

        Department department =
                departmentRepository
                        .findById(
                                request.getDepartmentId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found"
                                ));

        boolean exists =
                timeSlotRuleRepository
                        .findByDepartment_Id(
                                department.getId()
                        )
                        .isPresent();

        if (exists) {

            throw new RuntimeException(
                    "Department already has rule"
            );
        }

        TimeSlotRule rule =
                TimeSlotRule.builder()
                        .department(department)
                        .slotDuration(
                                request.getSlotDuration()
                        )
                        .workingStart(
                                request.getWorkingStart()
                        )
                        .workingEnd(
                                request.getWorkingEnd()
                        )
                        .lunchStart(
                                request.getLunchStart()
                        )
                        .lunchEnd(
                                request.getLunchEnd()
                        )
                        .maxPatientsPerDay(
                                request.getMaxPatientsPerDay()
                        )
                        .build();

        return mapToResponse(
                timeSlotRuleRepository.save(rule)
        );
    }

    @Override
    public TimeSlotRuleResponse update(
            Long id,
            TimeSlotRuleRequest request
    ) {

        TimeSlotRule rule =
                timeSlotRuleRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Rule not found"
                                ));

        rule.setSlotDuration(
                request.getSlotDuration()
        );

        rule.setWorkingStart(
                request.getWorkingStart()
        );

        rule.setWorkingEnd(
                request.getWorkingEnd()
        );

        rule.setLunchStart(
                request.getLunchStart()
        );

        rule.setLunchEnd(
                request.getLunchEnd()
        );

        rule.setMaxPatientsPerDay(
                request.getMaxPatientsPerDay()
        );

        return mapToResponse(
                timeSlotRuleRepository.save(rule)
        );
    }

    @Override
    public void delete(Long id) {

        timeSlotRuleRepository.deleteById(id);
    }

    @Override
    public TimeSlotRuleResponse getById(
            Long id
    ) {

        TimeSlotRule rule =
                timeSlotRuleRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Rule not found"
                                ));

        return mapToResponse(rule);
    }

    @Override
        public TimeSlotRuleResponse getByDepartmentId(
                Long departmentId
        ) {

        TimeSlotRule rule =
                timeSlotRuleRepository
                        .findByDepartment_Id(departmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Rule not found"
                                ));

        return mapToResponse(rule);
        }

    @Override
    public List<TimeSlotRuleResponse> getAll() {

        return timeSlotRuleRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TimeSlotRuleResponse mapToResponse(
            TimeSlotRule rule
    ) {

        return TimeSlotRuleResponse.builder()
                .id(rule.getId())
                .departmentId(
                        rule.getDepartment().getId()
                )
                .departmentName(
                        rule.getDepartment().getName()
                )
                .slotDuration(
                        rule.getSlotDuration()
                )
                .workingStart(
                        rule.getWorkingStart()
                )
                .workingEnd(
                        rule.getWorkingEnd()
                )
                .lunchStart(
                        rule.getLunchStart()
                )
                .lunchEnd(
                        rule.getLunchEnd()
                )
                .maxPatientsPerDay(
                        rule.getMaxPatientsPerDay()
                )
                .build();
    }
}