package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.response.NotificationResponse;
import com.local.PhongKham.entity.Notification;
import com.local.PhongKham.repository.NotificationRepository;
import com.local.PhongKham.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl
        implements NotificationService {

    private final NotificationRepository
            notificationRepository;

    @Override
    public List<NotificationResponse>
            getMyNotifications(
                    Long accountId
            ) {

        return notificationRepository
                .findByAccountIdOrderByCreatedAtDesc(
                        accountId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private NotificationResponse mapToResponse(
            Notification notification
    ) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}