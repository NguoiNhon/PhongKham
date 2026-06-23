package com.local.PhongKham.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String email;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}