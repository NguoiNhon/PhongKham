package com.local.PhongKham.service;

import com.local.PhongKham.entity.Appointment;

import java.time.LocalDate;

public interface AutoAppointmentService {

    Appointment autoBook(
            Long patientId,
            String departmentName,
            LocalDate appointmentDate
    );
}