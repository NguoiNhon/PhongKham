package com.local.PhongKham.chatbot.service;

import com.local.PhongKham.chatbot.entity.ConversationSession;
import com.local.PhongKham.chatbot.enums.BookingStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    private final EntityExtractorService extractorService;

    public ChatbotService(
            EntityExtractorService extractorService
    ) {
        this.extractorService = extractorService;
    }

    public String handleMessage(String message) {

        String department =
                extractorService.extractDepartment(message);

        if (department.equals("UNKNOWN")) {
            return "Xin hãy cho biết chuyên khoa bạn muốn khám.";
        }

        return "Bạn muốn đặt lịch khoa: " + department;
    }
}