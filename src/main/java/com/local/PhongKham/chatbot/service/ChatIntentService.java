package com.local.PhongKham.chatbot.service;

import com.local.PhongKham.chatbot.enums.ChatIntent;

public interface ChatIntentService {

    ChatIntent detectIntent(String message);
}