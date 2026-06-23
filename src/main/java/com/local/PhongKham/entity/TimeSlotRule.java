package com.local.PhongKham.entity;

import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "time_slot_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "slot_duration", nullable = false)
    private Integer slotDuration;

    @Column(name = "Working_start")
    private LocalTime workingStart;

    @Column(name = "Working_end")
    private LocalTime workingEnd;

    @Column(name = "lunch_start")
    private LocalTime lunchStart;

    @Column(name = "lunch_end")
    private LocalTime lunchEnd;

    @Column(name = "max_patient_per_day")
    private Integer maxPatientsPerDay;
}
