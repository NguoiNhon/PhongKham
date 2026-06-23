package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.response.PatientResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.entity.Patient;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.repository.PatientRepository;
import com.local.PhongKham.service.PatientService;
import com.local.PhongKham.dto.request.PatientCreateRequest;
import com.local.PhongKham.dto.request.UpdatePatientRequest;
import com.local.PhongKham.enums.Role;
import java.util.List;


import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl
        implements PatientService {

    private final PatientRepository patientRepository;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public PatientResponse getMyProfile(
            String username
    ) {

        // tìm account
        Account account =
                accountRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"
                                ));

        // tìm patient
        Patient patient =
                patientRepository
                        .findByAccountId(account.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found"
                                ));

        // convert response
        return PatientResponse.builder()
                .username(account.getUsername())
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(account.getEmail())
                .build();
    }
    @Override
        public PatientResponse updateMyProfile(
                String username,
                UpdatePatientRequest request
        ) {

        // tìm account
        Account account =
                accountRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"
                                ));

        // tìm patient
        Patient patient =
                patientRepository
                        .findByAccountId(account.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found"
                                ));

        // update patient
        patient.setFullName(
                request.getFullName()
        );

        patient.setDateOfBirth(
                request.getDateOfBirth()
        );

        patient.setGender(
                request.getGender()
        );

        patient.setPhone(
                request.getPhone()
        );

        // update email account
        account.setEmail(
                request.getEmail()
        );

        patientRepository.save(patient);

        accountRepository.save(account);

        // response
        return PatientResponse.builder()
                .username(account.getUsername())
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .phone(patient.getPhone())
                .email(account.getEmail())
                .build();
        }
        @Override
        public PatientResponse create(
                PatientCreateRequest request
        ) {

        if (
                accountRepository.existsByUsername(
                request.getUsername()
                )
        ) {

                throw new RuntimeException(
                        "Username đã tồn tại"
                );
        }

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
                        .role(Role.PATIENT)
                        .build();

        account =
                accountRepository.save(
                        account
                );

        Patient patient =
                Patient.builder()
                        .account(account)
                        .fullName(
                                request.getFullName()
                        )
                        .phone(
                                request.getPhone()
                        )
                        .gender(
                                request.getGender()
                        )
                        .dateOfBirth(
                        request.getDateOfBirth()
                        )
                        .build();

        return mapToResponse(
                patientRepository.save(
                        patient
                )
        );
        }
        private PatientResponse mapToResponse(
                Patient patient
        ) {

        Account account =
                patient.getAccount();

        return PatientResponse.builder()

                .id(patient.getId())

                .username(
                        account != null
                                ? account.getUsername()
                                : null
                )

                .email(
                        account != null
                                ? account.getEmail()
                                : null
                )

                .fullName(
                        patient.getFullName()
                )

                .phone(
                        patient.getPhone()
                )

                .gender(
                        patient.getGender()
                )

                .dateOfBirth(
                        patient.getDateOfBirth()
                )

                .build();
        }
        @Override
        public List<PatientResponse> getAllPatients() {

        return patientRepository.findAll()
                .stream()
                .map(patient -> {

                        Account account =
                                patient.getAccount();

                        return PatientResponse.builder()
                                .id(patient.getId())
                                .fullName(patient.getFullName())
                                .dateOfBirth(patient.getDateOfBirth())
                                .gender(patient.getGender())
                                .phone(patient.getPhone())

                                // account
                                .accountId(account.getId())
                                .username(account.getUsername())
                                .email(account.getEmail())
                                .role(account.getRole())
                                .status(account.getStatus())

                                .build();
                })
                .toList();
        }
        @Override
        public PatientResponse update(
                Long id,
                UpdatePatientRequest request
        ) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy patient"));

        // update patient
        patient.setFullName(request.getFullName());
        patient.setGender(request.getGender());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());

        // update account
        Account account = patient.getAccount();

        if (account != null) {

                account.setEmail(
                        request.getEmail()
                );

                accountRepository.save(account);
        }

        Patient updated =
                patientRepository.save(patient);

        return PatientResponse.builder()
                .id(updated.getId())
                .fullName(updated.getFullName())
                .gender(updated.getGender())
                .phone(updated.getPhone())
                .dateOfBirth(updated.getDateOfBirth())

                .username(
                        account != null
                                ? account.getUsername()
                                : null
                )

                .email(
                        account != null
                                ? account.getEmail()
                                : null
                )
                .build();
        }
}