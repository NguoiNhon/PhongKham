package com.local.PhongKham.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String gender;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "image_url")
    private String imageUrl;


    // department_id
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}