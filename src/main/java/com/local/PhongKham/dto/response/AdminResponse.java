package com.local.PhongKham.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AdminResponse {

    private Long id;

    private String username;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String gender;

    private LocalDate dateOfBirth;
}