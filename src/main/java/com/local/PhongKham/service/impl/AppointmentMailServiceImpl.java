package com.local.PhongKham.service.impl;

import com.local.PhongKham.entity.Appointment;
import com.local.PhongKham.service.AppointmentMailService;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentMailServiceImpl
        implements AppointmentMailService {

    private final JavaMailSender
            mailSender;

    @Override
    public void sendReminderEmail(
            Appointment appointment,
            String email
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Nhắc lịch khám"
        );

        message.setText(

                "Xin chào "
                + appointment
                    .getPatient()
                    .getFullName()

                + "\n\n"

                + "Bạn có lịch khám lúc "
                + appointment
                    .getStartTime()

                + " ngày "
                + appointment
                    .getAppointmentDate()

                + "\n"

                + "Vui lòng đến trước "
                + "15 phút.\n\n"

                + "Trung tâm Y tế "
                + "Quận Thanh Xuân"
        );

        mailSender.send(message);
    }
}