package com.local.PhongKham.controller;

import com.local.PhongKham.dto.response.NotificationResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService
            notificationService;

    private final AccountRepository
            accountRepository;

    @GetMapping("/me")
    public List<NotificationResponse>
            getMyNotifications(
                    Authentication authentication
            ) {

        String username =
                authentication.getName();

        Account account =
                accountRepository
                        .findByUsername(username)
                        .orElseThrow();

        return notificationService
                .getMyNotifications(
                        account.getId()
                );
    }
}