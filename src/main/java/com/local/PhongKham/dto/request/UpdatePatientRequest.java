package com.local.PhongKham.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePatientRequest {

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phone;

    private String email;
}