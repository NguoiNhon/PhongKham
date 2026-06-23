package com.local.PhongKham.dto.request;

import com.local.PhongKham.enums.Role;

import lombok.Data;

@Data
public class CreateAccountRequest {

    private String username;

    private String email;

    private String password;

    private Role role;
}