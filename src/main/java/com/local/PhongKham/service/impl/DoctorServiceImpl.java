package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.request.CreateDoctorRequest;
import com.local.PhongKham.dto.request.UpdateDoctorRequest;
import com.local.PhongKham.dto.response.DoctorResponse;
import com.local.PhongKham.entity.Appointment;
import com.local.PhongKham.entity.Department;
import com.local.PhongKham.entity.Doctor;
import com.local.PhongKham.entity.TimeSlotRule;
import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.repository.DepartmentRepository;
import com.local.PhongKham.repository.DoctorRepository;
import com.local.PhongKham.repository.TimeSlotRuleRepository;
import com.local.PhongKham.repository.AppointmentRepository;
import com.local.PhongKham.service.DoctorService;
import com.local.PhongKham.util.TimeSlot;
import com.local.PhongKham.util.TimeSlotGenerator;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.IOException;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

        private final DoctorRepository doctorRepository;
        private final DepartmentRepository departmentRepository;
        private final AppointmentRepository appointmentRepository;
        private final TimeSlotRuleRepository timeSlotRuleRepository;
        private final TimeSlotGenerator timeSlotGenerator;

    @Override
        public DoctorResponse create(CreateDoctorRequest request, MultipartFile file) {

        // 🔥 1. Upload ảnh
        String imageUrl = null;

        if (file != null && !file.isEmpty()) {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + fileName);

        try {
                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());
        } catch (IOException e) {
                throw new RuntimeException("Upload failed");
        }

        imageUrl = "/uploads/" + fileName;
        }
        // 🔥 3. Lấy department
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department không tồn tại"));



        // 🔥 5. Tạo doctor (QUAN TRỌNG)
        Doctor doctor = Doctor.builder()
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .phoneNumber(request.getPhoneNumber())
                .yearsOfExperience(request.getYearsOfExperience())
                .department(department)
                .imageUrl(imageUrl) // ✅ dùng ảnh upload
                .build();

        return mapToResponse(doctorRepository.save(doctor));
        }

    @Override
        public List<DoctorResponse> getAll(Long departmentId) {

        List<Doctor> doctors;

        if (departmentId != null) {
                doctors = doctorRepository.findByDepartment_Id(departmentId);
        } else {
                doctors = doctorRepository.findAll();
        }

        return doctors.stream()
                .map(this::mapToResponse)
                .toList();
        }

    @Override
    public DoctorResponse getById(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Doctor không tồn tại")
                );

        return mapToResponse(doctor);
    }

        @Override
        public DoctorResponse update(
                Long id,
                UpdateDoctorRequest request,
                MultipartFile file
        ) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor không tồn tại"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department không tồn tại"));

        // 🔥 UPDATE ẢNH (QUAN TRỌNG)
        if (file != null && !file.isEmpty()) {

                // 👉 XÓA ẢNH CŨ (nâng cao - optional nhưng nên có)
                try {
                if (doctor.getImageUrl() != null) {
                        Path oldPath = Paths.get("uploads/" +
                                doctor.getImageUrl().replace("/uploads/", ""));
                        Files.deleteIfExists(oldPath);
                }
                } catch (IOException e) {
                System.out.println("Không xóa được ảnh cũ");
                }

                // 👉 UPLOAD ẢNH MỚI
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path newPath = Paths.get("uploads/" + fileName);

                try {
                Files.createDirectories(newPath.getParent());
                Files.write(newPath, file.getBytes());
                } catch (IOException e) {
                throw new RuntimeException("Upload failed");
                }

                doctor.setImageUrl("/uploads/" + fileName);
        }
        // 🔥 UPDATE INFO
        doctor.setFullName(request.getFullName());
        doctor.setDateOfBirth(request.getDateOfBirth());
        doctor.setGender(request.getGender());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setYearsOfExperience(request.getYearsOfExperience());
        doctor.setDepartment(department);
        return mapToResponse(doctorRepository.save(doctor));
        }

        @Override
        public void delete(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor không tồn tại"));
        // 🔥 CHECK LỊCH KHÁM
        if (appointmentRepository.existsByDoctor_Id(id)) {
                throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Bác sĩ đã có lịch khám, không thể xoá"
                );
        }
        // 🔥 XÓA ẢNH
        try {
                if (doctor.getImageUrl() != null) {
                Path path = Paths.get("uploads/" +
                        doctor.getImageUrl().replace("/uploads/", ""));
                Files.deleteIfExists(path);
                }
        } catch (IOException e) {
                System.out.println("Không xóa được ảnh");
        }

        // 🔥 XÓA DB
        doctorRepository.delete(doctor);
        }
    private DoctorResponse mapToResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .dateOfBirth(doctor.getDateOfBirth())
                .gender(doctor.getGender())
                .phoneNumber(doctor.getPhoneNumber())
                .yearsOfExperience(
                        doctor.getYearsOfExperience()
                )
                .departmentName(
                        doctor.getDepartment() != null 
                                ? doctor.getDepartment().getName() 
                                : null
                )
                .imageUrl(
                        doctor.getImageUrl()
                )
                .build();
    }
    @Override
        public List<DoctorResponse> getAvailableDoctors(
                Long departmentId,
                LocalDate date
        ) {

        TimeSlotRule rule =
                timeSlotRuleRepository
                        .findByDepartment_Id(
                                departmentId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Rule not found"
                                )
                        );

        List<TimeSlot> slots =
                timeSlotGenerator.generateSlots(rule);

        List<Doctor> doctors =
                doctorRepository.findByDepartment_Id(
                        departmentId
                );

       return doctors.stream()
        .filter(doctor -> {

                List<Appointment> appointments =
                        appointmentRepository
                                .findByDoctorIdAndAppointmentDate(
                                        doctor.getId(),
                                        date
                                );

                return slots.stream().anyMatch(slot -> {

                boolean conflict = appointments.stream().anyMatch(a -> {

                        boolean overlap =
                                slot.getStart().isBefore(a.getEndTime()) &&
                                slot.getEnd().isAfter(a.getStartTime());

                        boolean active =
                                a.getAppointmentStatus() == AppointmentStatus.CONFIRMED ||
                                a.getAppointmentStatus() == AppointmentStatus.HOLD;

                        boolean holdValid =
                                a.getHoldExpiredAt() == null ||
                                a.getHoldExpiredAt().isAfter(LocalDateTime.now());

                        return overlap && active && holdValid;
                });

                return !conflict;
                });
        })
        .map(this::mapToResponse)
        .toList();
        } 
}