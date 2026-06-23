package com.local.PhongKham.chatbot.service.impl;

import com.local.PhongKham.chatbot.entity.ChatMessage;
import com.local.PhongKham.chatbot.enums.SenderType;
import com.local.PhongKham.chatbot.repository.ChatMessageRepository;
import com.local.PhongKham.chatbot.service.ChatMemoryService;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMemoryServiceImpl
        implements ChatMemoryService {

    @Autowired
    private ChatMessageRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<ChatMessage> getRecentMessages(
            Long accountId
    ) {

        return repository
                .findTop10ByAccountIdOrderByCreatedAtDesc(
                        accountId
                );
    }

    @Override
    public void saveMessage(
            Long accountId,
            String role,
            String content
    ) {

        Account account =
                accountRepository
                        .findById(accountId)
                        .orElseThrow();

        ChatMessage message =
                new ChatMessage();

        message.setAccount(account);

        message.setSenderType(
                role.equals("user")
                        ? SenderType.USER
                        : SenderType.ASSISTANT
        );

        message.setContent(content);

        message.setCreatedAt(
                LocalDateTime.now()
        );

        repository.save(message);
    }
}