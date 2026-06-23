package com.local.PhongKham.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientCreateRequest {

    // account
    private String username;

    private String password;

    private String email;

    // patient
    private String fullName;

    private String phone;

    private String gender;

    private LocalDate dateOfBirth;
}