package com.local.PhongKham.service;
import com.local.PhongKham.dto.response.PaymentResponse;
import com.local.PhongKham.entity.Appointment;
import java.util.Map;
import java.util.List;

public interface PaymentService {

    String createVnPayPayment(
        Appointment appointment,
        Long amount
);
    void handleVnPayCallback(
            Map<String, String> params
    );
    PaymentResponse getPaymentByAppointmentId(Long appointmentId);
}