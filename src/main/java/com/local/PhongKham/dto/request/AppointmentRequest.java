package com.local.PhongKham.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AppointmentRequest {

    private Long departmentId;

    private Long doctorId;

    private LocalDate appointmentDate;

    private String description;
}