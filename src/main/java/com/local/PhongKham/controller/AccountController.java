package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.ChangePasswordRequest;
import com.local.PhongKham.dto.request.RegisterRequest;
import com.local.PhongKham.dto.request.ResendOtpRequest;
import com.local.PhongKham.dto.response.AccountResponse;
import com.local.PhongKham.dto.response.RegisterResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.service.AccountService;
import com.local.PhongKham.dto.request.VerifyEmailRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        Account account = accountService.register(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFullName(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getPhone());

        return new RegisterResponse(
                account.getUsername(),
                account.getEmail(),
                "Đã gửi OTP xác thực email");
    }

    @PostMapping("/verify-email")
    public String verifyEmail(
            @RequestBody VerifyEmailRequest request) {

        accountService.verifyEmail(
                request.getEmail(),
                request.getOtp());

        return "Xác thực email thành công";
    }

    @GetMapping("/me")
    public AccountResponse getMyInfo(Authentication authentication) {

        String username = authentication.getName();

        return accountService.getMyInfo(username);
    }

    @PutMapping("/change-password")
    public String changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        accountService.changePassword(
                username,
                request.getOldPassword(),
                request.getNewPassword());

        return "Đổi mật khẩu thành công";
    }
    @PostMapping("/resend-verify-otp")
    public String resendVerifyOtp(
            @RequestBody ResendOtpRequest request
    ) {

        accountService.resendVerifyOtp(
                request.getEmail()
        );

        return "Đã gửi lại OTP";
    }
}