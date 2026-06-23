package com.local.PhongKham.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDoctorRequest {

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    private String email;

    private Integer yearsOfExperience;

    private Long departmentId;

    private String imageUrl;
}