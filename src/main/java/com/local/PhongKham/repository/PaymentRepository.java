package com.local.PhongKham.repository;

import com.local.PhongKham.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionRef(
            String transactionRef
    );
    Optional<Payment> findByAppointment_Id(Long appointmentId);
}