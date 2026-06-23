package com.local.PhongKham.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {

    private Long id;
    private String transactionRef;
    private Long amount;
    private String status;
    private String vnpTransactionNo;
    private String orderInfo;
    private LocalDateTime createdAt;

    private Long appointmentId;
}