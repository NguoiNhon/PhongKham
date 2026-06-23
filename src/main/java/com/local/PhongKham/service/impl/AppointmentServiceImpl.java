package com.local.PhongKham.service.impl;

import com.local.PhongKham.dto.request.AppointmentRequest;
import com.local.PhongKham.entity.*;
import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.enums.MedicalStatus;
import com.local.PhongKham.repository.*;
import com.local.PhongKham.service.AppointmentService;
import com.local.PhongKham.util.TimeSlot;
import com.local.PhongKham.util.TimeSlotGenerator;
import com.local.PhongKham.dto.response.AppointmentResponse;
import com.local.PhongKham.service.PaymentService;


import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final TimeSlotRuleRepository timeSlotRuleRepository;

    private final TimeSlotGenerator timeSlotGenerator;

    private final AccountRepository accountRepository;

    private final PaymentService paymentService;

    

    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        if (request.getDoctorId() == null) {
        throw new RuntimeException(
                "Vui lòng chọn bác sĩ"
        );
        }

        // Lấy user đang login
        Authentication authentication =
        SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username =
        authentication.getName();

        Account account =
        accountRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found"
                        ));

        Patient patient =
                patientRepository
                        .findByAccountId(account.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found"
                                ));

        // Lấy rule của chuyên khoa
        TimeSlotRule rule =
                timeSlotRuleRepository
                        .findByDepartment_Id(
                                request.getDepartmentId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Rule not found"
                                ));

        Doctor doctor =
        doctorRepository.findById(
                request.getDoctorId()
        ).orElseThrow(
                () -> new RuntimeException(
                "Doctor not found"
                )
        );
        List<Appointment> existingAppointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentDate(
                                doctor.getId(),
                                request.getAppointmentDate()
                        );

        List<Appointment> patientAppointments =
                appointmentRepository
                        .findByPatientIdAndAppointmentDate(
                                patient.getId(),
                                request.getAppointmentDate()
                        );
        List<TimeSlot> slots =
                timeSlotGenerator.generateSlots(rule);

        for (TimeSlot slot : slots) {

        boolean conflict = false;

        for (Appointment existing : existingAppointments) {

                boolean overlap =
                        slot.getStart().isBefore(
                                existing.getEndTime()
                        )
                        &&
                        slot.getEnd().isAfter(
                                existing.getStartTime()
                        );

                boolean active =
                        existing.getAppointmentStatus()
                                == AppointmentStatus.CONFIRMED
                        ||
                        existing.getAppointmentStatus()
                                == AppointmentStatus.HOLD;

                boolean holdValid =
                        existing.getHoldExpiredAt() == null
                        ||
                        existing.getHoldExpiredAt()
                                .isAfter(LocalDateTime.now());

                if (overlap && active && holdValid) {

                conflict = true;
                break;
                }
        }

        for (Appointment existing : patientAppointments) {

                boolean overlap =
                        slot.getStart().isBefore(
                                existing.getEndTime()
                        )
                        &&
                        slot.getEnd().isAfter(
                                existing.getStartTime()
                        );

                boolean active =
                        existing.getAppointmentStatus()
                                == AppointmentStatus.CONFIRMED
                        ||
                        existing.getAppointmentStatus()
                                == AppointmentStatus.HOLD;

                boolean holdValid =
                        existing.getHoldExpiredAt() == null
                        ||
                        existing.getHoldExpiredAt()
                                .isAfter(LocalDateTime.now());

                if (overlap && active && holdValid) {

                conflict = true;
                break;
                }
        }

        if (!conflict) {

                Appointment appointment =
                        Appointment.builder()
                                .patient(patient)
                                .doctor(doctor)
                                .timeSlotRule(rule)
                                .appointmentDate(
                                        request.getAppointmentDate()
                                )
                                .startTime(
                                        slot.getStart()
                                )
                                .endTime(
                                        slot.getEnd()
                                )
                                .appointmentStatus(
                                        AppointmentStatus.HOLD
                                )
                                .holdExpiredAt(
                                        LocalDateTime.now()
                                                .plusMinutes(10)
                                )
                                .medicalStatus(
                                        MedicalStatus.NOT_EXAMINED
                                )
                                .description(
                                        request.getDescription()
                                )
                                .build();

                Appointment savedAppointment =
                        appointmentRepository
                                .save(appointment);

                String paymentUrl =
                        paymentService.createVnPayPayment(
                                savedAppointment,
                                200000L
                        );

                return AppointmentResponse.builder()
                        .appointmentId(
                                savedAppointment.getId()
                        )
                        .patientId(
                                patient.getId()
                        )
                        .patientName(
                                patient.getFullName()
                        )
                        .doctorId(
                                doctor.getId()
                        )
                        .doctorName(
                                doctor.getFullName()
                        )
                        .departmentName(
                                doctor.getDepartment().getName()
                        )
                        .appointmentDate(
                                savedAppointment.getAppointmentDate()
                        )
                        .startTime(
                                savedAppointment.getStartTime()
                        )
                        .endTime(
                                savedAppointment.getEndTime()
                        )
                        .appointmentStatus(
                                savedAppointment.getAppointmentStatus()
                        )
                        .medicalStatus(
                                savedAppointment.getMedicalStatus()
                        )
                        .description(
                                savedAppointment.getDescription()
                        )
                        .paymentUrl(
                                paymentUrl
                        )
                        .build();
        }
        }

        throw new RuntimeException(
                "Không còn lịch trống"
        );
    }
        
        @Override
        public List<AppointmentResponse> getMyAppointments(String username) {

        // Tìm account
        Account account =
                accountRepository
                        .findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"
                                ));

        // Tìm patient
        Patient patient =
                patientRepository
                        .findByAccountId(account.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found"
                                ));

        // Lấy appointments
        List<Appointment> appointments =
        appointmentRepository
                .findByPatient_IdAndAppointmentStatusAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
                        patient.getId(),
                        AppointmentStatus.CONFIRMED,
                        LocalDate.now()
                );
        // Convert response
        return appointments.stream()
                .map(appointment ->
                        AppointmentResponse.builder()
                                .appointmentId(
                                        appointment.getId()
                                )
                                .patientId(
                                        patient.getId()
                                )
                                .patientName(
                                        patient.getFullName()
                                )
                                .doctorId(
                                        appointment.getDoctor().getId()
                                )
                                .doctorName(
                                        appointment.getDoctor().getFullName()
                                )
                                .departmentName(
                                        appointment
                                                .getDoctor()
                                                .getDepartment()
                                                .getName()
                                )
                                .appointmentDate(
                                        appointment.getAppointmentDate()
                                )
                                .startTime(
                                        appointment.getStartTime()
                                )
                                .endTime(
                                        appointment.getEndTime()
                                )
                                .appointmentStatus(
                                        appointment.getAppointmentStatus()
                                )
                                .medicalStatus(
                                        appointment.getMedicalStatus()
                                )
                                .description(
                                        appointment.getDescription()
                                        )
                                .build()
                )
                .toList();
        }
        @Override
        public List<AppointmentResponse> getAllAppointments() {

        List<Appointment> appointments =
                appointmentRepository.findAll();

        return appointments.stream()
                .map(appointment ->
                        AppointmentResponse.builder()
                                .appointmentId(
                                        appointment.getId()
                                )
                                .patientId(
                                        appointment.getPatient().getId()
                                )
                                .patientName(
                                        appointment.getPatient().getFullName()
                                )
                                .doctorId(
                                        appointment.getDoctor().getId()
                                )
                                .doctorName(
                                        appointment.getDoctor().getFullName()
                                )
                                .departmentName(
                                        appointment
                                                .getDoctor()
                                                .getDepartment()
                                                .getName()
                                )
                                .appointmentDate(
                                        appointment.getAppointmentDate()
                                )
                                .startTime(
                                        appointment.getStartTime()
                                )
                                .endTime(
                                        appointment.getEndTime()
                                )
                                .appointmentStatus(
                                        appointment.getAppointmentStatus()
                                )
                                .medicalStatus(
                                        appointment.getMedicalStatus()
                                )
                                .description(
                                        appointment.getDescription()
                                        )
                                .build()
                )
                .toList();
        }
        @Override
        public void cancelAppointment(
                Long appointmentId,
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

        // tìm appointment
        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found"
                                ));

        // check chủ lịch
        if (!appointment
                .getPatient()
                .getId()
                .equals(patient.getId())) {

                throw new RuntimeException(
                        "Bạn không thể hủy lịch của người khác"
                );
        }

        // check đã hủy chưa
        if (appointment.getAppointmentStatus()
                == AppointmentStatus.CANCELLED) {

                throw new RuntimeException(
                        "Lịch khám đã được hủy trước đó"
                );
        }

        // update status
        appointment.setAppointmentStatus(
                AppointmentStatus.CANCELLED
        );

        appointmentRepository.save(appointment);
        }
        @Override
        public AppointmentResponse getById(
                Long appointmentId
        ) {

        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found"
                                ));

        return AppointmentResponse.builder()
                .appointmentId(
                        appointment.getId()
                )
                .patientId(
                        appointment.getPatient().getId()
                )
                .patientName(
                        appointment.getPatient().getFullName()
                )
                .doctorId(
                        appointment.getDoctor().getId()
                )
                .doctorName(
                        appointment.getDoctor().getFullName()
                )
                .departmentName(
                        appointment
                                .getDoctor()
                                .getDepartment()
                                .getName()
                )
                .appointmentDate(
                        appointment.getAppointmentDate()
                )
                .startTime(
                        appointment.getStartTime()
                )
                .endTime(
                        appointment.getEndTime()
                )
                .appointmentStatus(
                        appointment.getAppointmentStatus()
                )
                .medicalStatus(
                        appointment.getMedicalStatus()
                )
                .description(
                        appointment.getDescription()
                )
                .build();
        }
        @Override
        public void updateMedicalStatus(
                Long appointmentId,
                MedicalStatus medicalStatus,
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

        // tìm appointment
        Appointment appointment =
                appointmentRepository
                        .findById(appointmentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Appointment not found"
                                ));

        // chỉ cho cập nhật đúng ngày khám
        if (!appointment.getAppointmentDate().equals(
                java.time.LocalDate.now()
        )) {

                throw new RuntimeException(
                        "Chỉ được cập nhật trong ngày khám"
                );
        }

        if (
                appointment.getAppointmentStatus()
                        == AppointmentStatus.HOLD
                ||
                appointment.getAppointmentStatus()
                        == AppointmentStatus.CANCELLED
        ) {

        throw new RuntimeException(
                "Không thể cập nhật trạng thái khám cho lịch này"
        );
        }
        // nếu là doctor -> chỉ được sửa lịch của mình

        // receptionist/admin được sửa tất cả
        else if (
                !account.getRole().name().equals("ADMIN")
                &&
                !account.getRole().name().equals("RECEPTIONIST")
        ) {

                throw new RuntimeException(
                        "Bạn không có quyền cập nhật"
                );
        }

        // update medical status
        appointment.setMedicalStatus(
                medicalStatus
        );

        // tự động đổi trạng thái lịch
        if (medicalStatus == MedicalStatus.DONE) {

                appointment.setAppointmentStatus(
                        AppointmentStatus.CONFIRMED
                );
        }

        appointmentRepository.save(appointment);
        }
}