package com.local.PhongKham.dto.response;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotRuleResponse {

    private Long id;

    private Long departmentId;

    private String departmentName;

    private Integer slotDuration;

    private LocalTime workingStart;

    private LocalTime workingEnd;

    private LocalTime lunchStart;

    private LocalTime lunchEnd;

    private Integer maxPatientsPerDay;
}