package com.local.PhongKham.service;

import com.local.PhongKham.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse>
        getMyNotifications(
            Long accountId
        );
}