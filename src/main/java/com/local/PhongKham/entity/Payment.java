package com.local.PhongKham.entity;

import com.local.PhongKham.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // mã giao dịch bên mình
    @Column(name = "transaction_ref", unique = true)
    private String transactionRef;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    // mã giao dịch VNPAY
    private String vnpTransactionNo;

    private String orderInfo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // payment thuộc appointment nào
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = PaymentStatus.HOLD;
        }
    }
}