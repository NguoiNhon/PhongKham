package com.local.PhongKham.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DoctorResponse {

    private Long departmentId;

    private Long id;

    private String fullName;

    private LocalDate dateOfBirth;

    private String gender;

    private String qualification;

    private String phoneNumber;

    private Integer yearsOfExperience;

    private String departmentName;

    private String imageUrl;
}