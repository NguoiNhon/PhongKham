package com.local.PhongKham.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateDoctorRequest {

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    private Integer yearsOfExperience;

    private Long departmentId;

    private String imageUrl;
}