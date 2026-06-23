package com.local.PhongKham.chatbot.service.impl;

import com.local.PhongKham.chatbot.enums.ChatIntent;
import com.local.PhongKham.chatbot.service.ChatIntentService;

import org.springframework.stereotype.Service;

@Service
public class ChatIntentServiceImpl
        implements ChatIntentService {

    @Override
    public ChatIntent detectIntent(
            String message
    ) {

        String msg =
                message.toLowerCase();

        // ================= ĐẶT LỊCH =================
        if (
                msg.contains("đặt lịch")
                || msg.contains("đặt khám")
                || msg.contains("đăng ký khám")
        ) {

            return ChatIntent.BOOK_APPOINTMENT;
        }

        // ================= LỊCH BÁC SĨ =================
        if (
                msg.contains("lịch bác sĩ")
        ) {

            return ChatIntent.DOCTOR_VIEW_SCHEDULE;
        }

        // ================= ADMIN =================
        if (
                msg.contains("tất cả lịch khám")
        ) {

            return ChatIntent.ADMIN_VIEW_ALL_APPOINTMENTS;
        }

        // ================= LỊCH KHÁM =================
        if (
                msg.contains("lịch khám")
                || msg.contains("lịch hẹn")
        ) {

            return ChatIntent.CHECK_MY_APPOINTMENTS;
        }

        // ================= HỦY =================
        if (
                msg.contains("hủy lịch")
        ) {

            return ChatIntent.CANCEL_APPOINTMENT;
        }

        // ================= BÁC SĨ =================
        if (
                msg.contains("bác sĩ")
        ) {

            return ChatIntent.FIND_DOCTOR;
        }

        // ================= THANH TOÁN =================
        if (
                msg.contains("thanh toán")
        ) {

            return ChatIntent.CHECK_PAYMENT;
        }

        // ================= HỒ SƠ =================
        if (
                msg.contains("hồ sơ")
                || msg.contains("thông tin cá nhân")
        ) {

            return ChatIntent.GET_MY_PROFILE;
        }

        return ChatIntent.GENERAL_CHAT;
    }
}