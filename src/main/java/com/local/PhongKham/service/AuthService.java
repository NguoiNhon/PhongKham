package com.local.PhongKham.service;
import com.local.PhongKham.dto.request.ForgotPasswordRequest;
import com.local.PhongKham.dto.request.ResetPasswordRequest;


public interface AuthService {
    String login(String username, String password);
    String forgotPassword(
            ForgotPasswordRequest request
    );

    String resetPassword(
            ResetPasswordRequest request
    );
}
