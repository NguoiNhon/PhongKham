package com.local.PhongKham.scheduler;

import com.local.PhongKham.entity.Appointment;
import com.local.PhongKham.entity.Notification;
import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.repository.AppointmentRepository;
import com.local.PhongKham.repository.NotificationRepository;
import com.local.PhongKham.service.AppointmentMailService;


import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentScheduler {

    private final AppointmentRepository
            appointmentRepository;

    private final NotificationRepository
            notificationRepository;

    private final AccountRepository
            accountRepository;

    private final AppointmentMailService
            appointmentMailService;


    // =========================
    // AUTO CANCEL HOLD
    // =========================

    @Scheduled(fixedRate = 60000)
    public void cancelExpiredAppointments() {

        List<Appointment> appointments =
                appointmentRepository
                        .findExpiredAppointments(
                                LocalDateTime.now()
                        );

        for (Appointment appointment
                : appointments) {

            appointment.setAppointmentStatus(
                    AppointmentStatus.CANCELLED
            );
        }

        appointmentRepository.saveAll(
                appointments
        );
    }

    // =========================
    // REMINDER
    // =========================

    @Scheduled(fixedRate = 60000)
    public void remindAppointments() {

        LocalDateTime now =
                LocalDateTime.now();

        LocalDateTime remindTime =
                now.plusMinutes(30);

        List<Appointment> appointments =
                appointmentRepository.findAll();

        for (Appointment appointment
                : appointments) {

            // chỉ reminder lịch confirmed
            if (appointment.getAppointmentStatus()
                    != AppointmentStatus.CONFIRMED) {

                continue;
            }

            // đã reminder rồi
            if (Boolean.TRUE.equals(
                    appointment.getIsReminded()
            )) {

                continue;
            }

            LocalDateTime appointmentDateTime =
                    LocalDateTime.of(
                            appointment.getAppointmentDate(),
                            appointment.getStartTime()
                    );

            // lịch nằm trong 30 phút tới
            if (
                appointmentDateTime.isAfter(now)
                &&
                appointmentDateTime.isBefore(
                        remindTime
                )
            ) {

                Notification notification =
                        Notification.builder()
                                .account(
                                    accountRepository.findById(
                                        appointment
                                            .getPatient()
                                            .getAccount().getId()
                                    ).orElseThrow()
                                )
                                .title(
                                    "Nhắc lịch khám"
                                )
                                .message(
                                    "Bạn có lịch khám lúc "
                                    + appointment.getStartTime()
                                    + " ngày "
                                    + appointment.getAppointmentDate()
                                )
                                .isRead(false)
                                .createdAt(
                                    LocalDateTime.now()
                                )
                                .build();

                notificationRepository.save(
                        notification
                );
                String email =
                        accountRepository
                            .findById(
                                appointment
                                    .getPatient()
                                    .getAccount().getId()
                            )
                            .orElseThrow()
                            .getEmail();

                appointmentMailService
                        .sendReminderEmail(
                                appointment,
                                email
                        );

                appointment.setIsReminded(
                        true
                );

                appointmentRepository.save(
                        appointment
                );

                System.out.println(
                    "Đã gửi reminder appointment id = "
                    + appointment.getId()
                );
            }
        }
    }
}