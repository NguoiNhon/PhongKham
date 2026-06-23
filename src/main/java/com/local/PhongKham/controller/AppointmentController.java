package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.AppointmentRequest;
import com.local.PhongKham.dto.request.UpdateMedicalStatusRequest;
import com.local.PhongKham.service.AppointmentService;
import com.local.PhongKham.dto.response.AppointmentResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> create(
            @RequestBody AppointmentRequest request
    ) {

        AppointmentResponse response =
                appointmentService.create(request);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/patient/me")
    public List<AppointmentResponse> getMyAppointments(
            Authentication authentication
    ) {

        String username = authentication.getName();

        return appointmentService.getMyAppointments(username);
    }
    
    @GetMapping
    public List<AppointmentResponse> getAllAppointments() {

        return appointmentService.getAllAppointments();
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(
            @PathVariable Long id,
            Authentication authentication
    ) {

        String username = authentication.getName();

        appointmentService.cancelAppointment(id, username);

        return ResponseEntity.ok("Hủy lịch khám thành công");
    }
    @GetMapping("/{id}")
    public AppointmentResponse getById(
            @PathVariable Long id
    ) {

        return appointmentService.getById(id);
    }
    @PutMapping("/{id}/medical-status")
    public ResponseEntity<String> updateMedicalStatus(
            @PathVariable Long id,
            @RequestBody UpdateMedicalStatusRequest request,
            Authentication authentication
    ) {

        String username = authentication.getName();

        appointmentService.updateMedicalStatus(
                id,
                request.getMedicalStatus(),
                username
        );

        return ResponseEntity.ok(
                "Cập nhật trạng thái khám thành công"
        );
    }
}