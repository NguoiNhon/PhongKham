package com.local.PhongKham.chatbot.service;

import com.local.PhongKham.chatbot.entity.ChatMessage;

import java.util.List;

public interface ChatMemoryService {

    List<ChatMessage> getRecentMessages(
            Long accountId
    );

    void saveMessage(
            Long accountId,
            String role,
            String content
    );
}