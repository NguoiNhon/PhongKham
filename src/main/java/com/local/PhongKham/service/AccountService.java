package com.local.PhongKham.service;

import java.time.LocalDate;

import com.local.PhongKham.dto.response.AccountResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.enums.Role;
import java.util.List;

public interface AccountService {
    Account register(
        String username,
        String email,
        String password,
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String phone);

    AccountResponse getMyInfo(String username);
    
    void changePassword(String username,String oldPassword,String newPassword);
    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(Long id);

    AccountResponse createAccountByAdmin(
            String username,
            String email,
            String password,
            Role role
    );

    AccountResponse updateAccount(
            Long id,
            String email,
            Role role,
            AccountStatus status
    );

    void verifyEmail(String email, String otp);

    void resendVerifyOtp(String email);

    void deactivateAccount(Long id);

    void activateAccount(Long id);
}