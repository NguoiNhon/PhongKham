package com.local.PhongKham.chatbot.repository;

import com.local.PhongKham.chatbot.entity.ChatMessage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage>
    findTop10ByAccountIdOrderByCreatedAtDesc(
            Long accountId
    );
}