package com.local.PhongKham.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdminRequest {

    private String username;

    private String password;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;
}