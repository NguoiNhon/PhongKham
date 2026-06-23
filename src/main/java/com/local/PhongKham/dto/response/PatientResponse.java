package com.local.PhongKham.dto.response;


import lombok.*;

import java.time.LocalDate;

import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {

    private String username;

    private Long id;

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phone;

    private String email;

    private Long accountId;

    private Role role;

    private AccountStatus status;
}