package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.LoginRequest;
import com.local.PhongKham.dto.response.LoginResponse;
import com.local.PhongKham.service.AuthService;
import com.local.PhongKham.dto.request.ForgotPasswordRequest;
import com.local.PhongKham.dto.request.ResetPasswordRequest;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String token = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return new LoginResponse(token);
    }
    @PostMapping("/forgot-password")
    public String forgotPassword(
            @RequestBody
            ForgotPasswordRequest request
    ) {

        return authService
                .forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestBody
            ResetPasswordRequest request
    ) {

        return authService
                .resetPassword(request);
    }
}