package com.local.PhongKham.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyEmailRequest {

    private String email;

    private String otp;
}