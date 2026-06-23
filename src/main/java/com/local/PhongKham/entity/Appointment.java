package com.local.PhongKham.entity;

import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.enums.MedicalStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @Enumerated(EnumType.STRING)
    private MedicalStatus medicalStatus;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "time_slot_rule_id")
    private TimeSlotRule timeSlotRule;

    @Column(name = "appointment_date")
    private LocalDate appointmentDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "hold_expired_at")
    private LocalDateTime holdExpiredAt;

    @Column(name = "is_reminded")
    private Boolean isReminded; 
}