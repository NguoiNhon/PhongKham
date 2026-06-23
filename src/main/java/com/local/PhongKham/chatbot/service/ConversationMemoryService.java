package com.local.PhongKham.chatbot.service;

import com.local.PhongKham.chatbot.entity.ConversationSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConversationMemoryService {

    private final Map<String, ConversationSession> sessions = new HashMap<>();

    public ConversationSession getSession(String userId) {

        return sessions.computeIfAbsent(
                userId,
                id -> new ConversationSession()
        );
    }

    public void clearSession(String userId) {
        sessions.remove(userId);
    }
}