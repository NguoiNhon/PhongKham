package com.local.PhongKham.controller;

import com.local.PhongKham.dto.request.CreateDoctorRequest;
import com.local.PhongKham.dto.request.UpdateDoctorRequest;
import com.local.PhongKham.dto.response.DoctorResponse;
import com.local.PhongKham.service.DoctorService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DoctorResponse create(
            @RequestPart("data") CreateDoctorRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        return doctorService.create(request, file);
    }

    @GetMapping
    public List<DoctorResponse> getAll(
            @RequestParam(required = false) Long departmentId
    ) {
        return doctorService.getAll(departmentId);
    }

    @GetMapping("/{id}")
    public DoctorResponse getById(
            @PathVariable Long id
    ) {

        return doctorService.getById(id);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DoctorResponse update(
            @PathVariable Long id,
            @RequestPart("data") UpdateDoctorRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return doctorService.update(id, request, file);
    }

    @DeleteMapping("/{id}")
    public String delete(
            @PathVariable Long id
    ) {

        doctorService.delete(id);

        return "Xóa bác sĩ thành công";
    }
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 🔥 tạo tên file tránh trùng
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // 🔥 đường dẫn lưu
            Path path = Paths.get("uploads/" + fileName);

            // tạo folder nếu chưa có
            Files.createDirectories(path.getParent());

            // lưu file
            Files.write(path, file.getBytes());

            // trả về URL
            return "/uploads/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Upload failed");
        }
    }
    @GetMapping("/available")
    public List<DoctorResponse> getAvailableDoctors(
            @RequestParam Long departmentId,
            @RequestParam String date
    ) {
        return doctorService.getAvailableDoctors(
                departmentId,
                LocalDate.parse(date)
        );
    }
}