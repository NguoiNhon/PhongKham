package com.local.PhongKham.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.local.PhongKham.dto.request.CreateAccountRequest;
import com.local.PhongKham.dto.request.UpdateAccountRequest;
import com.local.PhongKham.dto.response.AccountResponse;
import com.local.PhongKham.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountResponse getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    public AccountResponse createAccount(
            @RequestBody CreateAccountRequest request
    ) {

        return accountService.createAccountByAdmin(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccount(
            @PathVariable Long id,
            @RequestBody UpdateAccountRequest request
    ) {

        return accountService.updateAccount(
                id,
                request.getEmail(),
                request.getRole(),
                request.getStatus()
        );
    }

    @DeleteMapping("/{id}")
    public String deactivateAccount(@PathVariable Long id) {

        accountService.deactivateAccount(id);

        return "Khóa tài khoản thành công";
    }
    @PutMapping("/{id}/activate")
    public String activateAccount(
            @PathVariable Long id
    ) {

        accountService.activateAccount(id);

        return "Mở khóa tài khoản thành công";
    }
}