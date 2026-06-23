package com.local.PhongKham.service;

import com.local.PhongKham.entity.Appointment;

public interface AppointmentMailService {

    void sendReminderEmail(
            Appointment appointment,
            String email
    );
}