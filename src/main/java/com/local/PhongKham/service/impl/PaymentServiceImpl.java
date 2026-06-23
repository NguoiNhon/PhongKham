package com.local.PhongKham.service.impl;

import com.local.PhongKham.config.VnPayConfig;
import com.local.PhongKham.service.PaymentService;
import com.local.PhongKham.util.VnPayUtil;
import com.local.PhongKham.entity.Appointment;
import com.local.PhongKham.repository.PaymentRepository;
import com.local.PhongKham.enums.PaymentStatus;
import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.repository.AppointmentRepository;
import com.local.PhongKham.entity.Payment;
import org.springframework.transaction.annotation.Transactional;

import com.local.PhongKham.dto.request.UpdatePatientRequest;
import com.local.PhongKham.dto.response.PatientResponse;
import com.local.PhongKham.dto.response.PaymentResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.entity.Patient;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {

    private final VnPayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final AccountRepository accountRepository;
    private final PatientRepository patientRepository;

    @Override
    public String createVnPayPayment(
        Appointment appointment,
        Long amount
    ) {

        String vnpTxnRef =
                String.valueOf(
                        System.currentTimeMillis()
                );

        String orderInfo =
                "Thanh toan lich kham #" +
                        appointment.getId();
        Payment payment =
                Payment.builder()
                        .transactionRef(vnpTxnRef)
                        .amount(amount)
                        .orderInfo(orderInfo)
                        .appointment(appointment)
                        .build();

        paymentRepository.save(payment);

        String ipAddr = "127.0.0.1";

        Map<String, String> vnpParams =
                new HashMap<>();

        vnpParams.put("vnp_Version", "2.1.0");

        vnpParams.put("vnp_Command", "pay");

        vnpParams.put(
                "vnp_TmnCode",
                vnPayConfig.tmnCode
        );

        vnpParams.put(
                "vnp_Amount",
                String.valueOf(amount * 100)
        );

        vnpParams.put(
                "vnp_CurrCode",
                "VND"
        );

        vnpParams.put(
                "vnp_TxnRef",
                vnpTxnRef
        );

        vnpParams.put(
                "vnp_OrderInfo",
                orderInfo
        );

        vnpParams.put(
                "vnp_OrderType",
                "other"
        );

        vnpParams.put(
                "vnp_Locale",
                "vn"
        );

        vnpParams.put(
                "vnp_ReturnUrl",
                vnPayConfig.returnUrl
        );

        vnpParams.put(
                "vnp_IpAddr",
                ipAddr
        );

        Calendar cld =
                Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyyMMddHHmmss");

        String createDate =
                formatter.format(cld.getTime());

        vnpParams.put(
                "vnp_CreateDate",
                createDate
        );

        String queryUrl =
                VnPayUtil.getPaymentURL(
                        vnpParams,
                        true
                );

        String hashData =
                VnPayUtil.getPaymentURL(
                        vnpParams,
                        false
                );

        String secureHash =
                VnPayUtil.hmacSHA512(
                        vnPayConfig.hashSecret,
                        hashData
                );

        queryUrl +=
                "&vnp_SecureHash=" + secureHash;

        return vnPayConfig.payUrl
                + "?"
                + queryUrl;
    }
    @Override
    @Transactional
        public void handleVnPayCallback(
                Map<String, String> params
        ) {

        String txnRef =
                params.get("vnp_TxnRef");

        String responseCode =
                params.get("vnp_ResponseCode");

        Payment payment =
                paymentRepository
                        .findByTransactionRef(txnRef)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found"
                                )
                        );

        if ("00".equals(responseCode)) {

                payment.setStatus(
                        PaymentStatus.SUCCESS
                );

                Appointment appointment =
                        payment.getAppointment();

                appointment.setAppointmentStatus(
                        AppointmentStatus.CONFIRMED
                );

                appointment.setHoldExpiredAt(null);

                appointmentRepository.save(appointment);

        } else {

                payment.setStatus(
                        PaymentStatus.FAILED
                );
        }

        payment.setVnpTransactionNo(
                params.get("vnp_TransactionNo")
        );

        paymentRepository.save(payment);
        }
        @Override
        public PaymentResponse getPaymentByAppointmentId(
                Long appointmentId
        ) {

        Payment p = paymentRepository
                .findByAppointment_Id(appointmentId)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hoá đơn")
                );

        return PaymentResponse.builder()
                .id(p.getId())
                .transactionRef(p.getTransactionRef())
                .amount(p.getAmount())
                .status(p.getStatus().name())
                .vnpTransactionNo(p.getVnpTransactionNo())
                .orderInfo(p.getOrderInfo())
                .createdAt(p.getCreatedAt())
                .appointmentId(p.getAppointment().getId())
                .build();
        }
}