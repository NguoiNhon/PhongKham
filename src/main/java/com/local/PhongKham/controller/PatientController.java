package com.local.PhongKham.controller;

import com.local.PhongKham.dto.response.PatientResponse;
import com.local.PhongKham.service.PatientService;
import com.local.PhongKham.dto.request.PatientCreateRequest;
import com.local.PhongKham.dto.request.UpdatePatientRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
        public ResponseEntity<?> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
        }

    @GetMapping("/me")
        public ResponseEntity<PatientResponse> getMyProfile(
                Authentication authentication
        ) {

                String username =
                        authentication.getName();

                return ResponseEntity.ok(
                        patientService.getMyProfile(
                                username
                        )
                );
        }
    @PutMapping("/me")
        public ResponseEntity<PatientResponse> updateMyProfile(
                @RequestBody UpdatePatientRequest request,
                Authentication authentication
        ) {

                String username =
                        authentication.getName();

                return ResponseEntity.ok(
                        patientService.updateMyProfile(
                                username,
                                request
                        )
                );
        }
    @PostMapping
        public PatientResponse create(
                @RequestBody
                PatientCreateRequest request
        ) {

        return patientService.create(request);
        }
        @PutMapping("/{id}")
        public ResponseEntity<PatientResponse> update(
                @PathVariable Long id,
                @RequestBody UpdatePatientRequest request
        ) {

        return ResponseEntity.ok(
                patientService.update(id, request)
        );
        }
}