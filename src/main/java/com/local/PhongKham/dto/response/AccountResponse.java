package com.local.PhongKham.dto.response;

import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {

    private Long id;

    private String username;

    private String email;

    private Role role;

    private AccountStatus status;
}