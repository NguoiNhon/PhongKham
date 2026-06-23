package com.local.PhongKham.dto.request;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotRuleRequest {

    private Long departmentId;

    private Integer slotDuration;

    private LocalTime workingStart;

    private LocalTime workingEnd;

    private LocalTime lunchStart;

    private LocalTime lunchEnd;

    private Integer maxPatientsPerDay;
}