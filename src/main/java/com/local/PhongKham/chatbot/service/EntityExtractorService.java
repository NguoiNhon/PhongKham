package com.local.PhongKham.chatbot.service;

import org.springframework.stereotype.Service;

@Service
public class EntityExtractorService {

    public String extractDepartment(String text) {

        text = text.toLowerCase();

        if (text.contains("tim")) {
            return "TIM_MACH";
        }

        if (text.contains("da")) {
            return "DA_LIEU";
        }

        if (text.contains("tai mũi họng")) {
            return "TAI_MUI_HONG";
        }

        return "UNKNOWN";
    }


    public String extractDate(String message) {

        message = message.toLowerCase();

        if (message.contains("ngày mai")) {
            return "TOMORROW";
        }

        if (message.contains("hôm nay")) {
            return "TODAY";
        }

        return null;
    }
}