package com.local.PhongKham.chatbot.service;

public interface OpenAIService {

    String ask(
            Long accountId,
            String message
    );
}