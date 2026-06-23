package com.local.PhongKham.dto.request;

import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.enums.Role;

import lombok.Data;

@Data
public class UpdateAccountRequest {

    private String email;

    private Role role;

    private AccountStatus status;
}