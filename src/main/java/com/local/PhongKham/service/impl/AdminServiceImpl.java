package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.request.AdminRequest;
import com.local.PhongKham.dto.response.AdminResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.entity.Admin;
import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.enums.Role;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.repository.AdminRepository;
import com.local.PhongKham.service.AdminService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl
        implements AdminService {

    private final AdminRepository adminRepository;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponse create(
            AdminRequest request
    ) {

        // check username
        if (accountRepository
                .findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException(
                    "Username already exists"
            );
        }

        // tạo account
        Account account =
                Account.builder()
                        .username(
                                request.getUsername()
                        )
                        .passwordHash(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .email(
                                request.getEmail()
                        )
                        .role(Role.ADMIN)
                        .status(
                                AccountStatus.ACTIVE
                        )
                        .build();

        Account savedAccount =
                accountRepository.save(account);

        // tạo admin
        Admin admin =
                Admin.builder()
                        .account(savedAccount)
                        .fullName(
                                request.getFullName()
                        )
                        .phoneNumber(
                                request.getPhoneNumber()
                        )
                        .gender(
                                request.getGender()
                        )
                        .dateOfBirth(
                                request.getDateOfBirth()
                        )
                        .email(
                                request.getEmail()
                        )
                        .build();

        Admin savedAdmin =
                adminRepository.save(admin);

        return mapToResponse(savedAdmin);
    }

    @Override
    public List<AdminResponse> getAll() {

        return adminRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public AdminResponse getById(Long id) {

        Admin admin =
                adminRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admin not found"
                                ));

        return mapToResponse(admin);
    }

    @Override
    public AdminResponse update(
            Long id,
            AdminRequest request
    ) {

        Admin admin =
                adminRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Admin not found"
                                ));

        admin.setFullName(
                request.getFullName()
        );

        admin.setPhoneNumber(
                request.getPhoneNumber()
        );

        admin.setGender(
                request.getGender()
        );

        admin.setDateOfBirth(
                request.getDateOfBirth()
        );

        admin.setEmail(
                request.getEmail()
        );

        // update email account
        admin.getAccount().setEmail(
                request.getEmail()
        );

        Admin updatedAdmin =
                adminRepository.save(admin);

        return mapToResponse(updatedAdmin);
    }
    
    private AdminResponse mapToResponse(
            Admin admin
    ) {

        return AdminResponse.builder()
                .id(admin.getId())
                .username(
                        admin.getAccount()
                                .getUsername()
                )
                .email(
                        admin.getEmail()
                )
                .fullName(
                        admin.getFullName()
                )
                .phoneNumber(
                        admin.getPhoneNumber()
                )
                .gender(
                        admin.getGender()
                )
                .dateOfBirth(
                        admin.getDateOfBirth()
                )
                .build();
    }
}