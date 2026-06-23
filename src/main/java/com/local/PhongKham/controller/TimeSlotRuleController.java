package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.TimeSlotRuleRequest;
import com.local.PhongKham.dto.response.TimeSlotRuleResponse;
import com.local.PhongKham.service.TimeSlotRuleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/time-slot-rules")
@RequiredArgsConstructor
public class TimeSlotRuleController {

    private final TimeSlotRuleService
            timeSlotRuleService;

    @PostMapping
    public ResponseEntity<TimeSlotRuleResponse>
    create(
            @RequestBody
            TimeSlotRuleRequest request
    ) {

        return ResponseEntity.ok(
                timeSlotRuleService.create(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeSlotRuleResponse>
    update(
            @PathVariable Long id,

            @RequestBody
            TimeSlotRuleRequest request
    ) {

        return ResponseEntity.ok(
                timeSlotRuleService.update(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id
    ) {

        timeSlotRuleService.delete(id);

        return ResponseEntity.ok(
                "Delete success"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlotRuleResponse>
    getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                timeSlotRuleService.getById(id)
        );
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<TimeSlotRuleResponse>
    getByDepartment(
            @PathVariable Long departmentId
    ) {

        return ResponseEntity.ok(
                timeSlotRuleService
                        .getByDepartmentId(departmentId)
        );
    }
    @GetMapping
    public ResponseEntity<List<TimeSlotRuleResponse>>
    getAll() {

        return ResponseEntity.ok(
                timeSlotRuleService.getAll()
        );
    }
    
}