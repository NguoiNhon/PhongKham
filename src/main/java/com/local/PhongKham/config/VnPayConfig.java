package com.local.PhongKham.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VnPayConfig {

    @Value("${vnpay.tmnCode}")
    public String tmnCode;

    @Value("${vnpay.hashSecret}")
    public String hashSecret;

    @Value("${vnpay.payUrl}")
    public String payUrl;

    @Value("${vnpay.returnUrl}")
    public String returnUrl;
}