package com.local.PhongKham.chatbot.controller;

import com.local.PhongKham.chatbot.dto.ChatRequest;
import com.local.PhongKham.chatbot.dto.ChatResponse;
import com.local.PhongKham.chatbot.service.OpenAIService;

import com.local.PhongKham.entity.Account;
import com.local.PhongKham.repository.AccountRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin("*")
public class ChatController {

    private final OpenAIService openAIService;

    private final AccountRepository accountRepository;

    public ChatController(
            OpenAIService openAIService,
            AccountRepository accountRepository
    ) {

        this.openAIService = openAIService;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request,
            Authentication authentication
    ) {

        try {

            String username =
                    authentication.getName();

            Account account =
                    accountRepository
                            .findByUsername(username)
                            .orElseThrow();

            String reply =
                    openAIService.ask(
                            account.getId(),
                            request.getMessage()
                    );

            return ResponseEntity.ok(
                    new ChatResponse(reply)
            );

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.ok(
                    new ChatResponse(
                            "Chatbot đang bận."
                    )
            );
        }
    }
}