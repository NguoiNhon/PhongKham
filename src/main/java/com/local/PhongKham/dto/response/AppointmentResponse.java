package com.local.PhongKham.dto.response;

import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.enums.MedicalStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class AppointmentResponse {

    private Long appointmentId;

    private Long patientId;

    private String patientName;

    private Long doctorId;

    private String doctorName;

    private String departmentName;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private AppointmentStatus appointmentStatus;

    private MedicalStatus medicalStatus;

    private String paymentUrl;

    private String description;
}