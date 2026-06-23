package com.local.PhongKham.controller;

import com.local.PhongKham.dto.response.PaymentResponse;
import com.local.PhongKham.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/vnpay-return")
    public String paymentReturn(
            @RequestParam Map<String, String> params
    ) {

        paymentService.handleVnPayCallback(params);

        String responseCode =
                params.get("vnp_ResponseCode");

        if (responseCode.equals("00")) {
            return "Thanh toán thành công";
        }

    return "Thanh toán thất bại";
}
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getPaymentByAppointment(
            @PathVariable Long appointmentId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentByAppointmentId(
                        appointmentId
                )
        );
    }
}