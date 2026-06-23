package com.local.PhongKham.service;

import java.util.List;

import com.local.PhongKham.dto.request.AppointmentRequest;
import com.local.PhongKham.dto.response.AppointmentResponse;
import com.local.PhongKham.enums.MedicalStatus;

public interface AppointmentService {
    AppointmentResponse create(AppointmentRequest request);
    List<AppointmentResponse> getMyAppointments(String username);
    List<AppointmentResponse> getAllAppointments();
    void cancelAppointment(Long appointmentId, String username);
    AppointmentResponse getById(Long appointmentId);
    void updateMedicalStatus(
        Long appointmentId,
        MedicalStatus medicalStatus,
        String username
);
}
