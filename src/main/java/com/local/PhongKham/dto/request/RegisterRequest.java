package com.local.PhongKham.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private String fullName;

    // Format: yyyy-MM-dd
    private LocalDate dateOfBirth;

    private String gender;

    private String phone;
}