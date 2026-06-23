package com.local.PhongKham.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.local.PhongKham.entity.Appointment;
import com.local.PhongKham.enums.AppointmentStatus;

import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

        List<Appointment> findByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );
        List<Appointment> findByPatientIdAndAppointmentDate(
            Long patientId,
            LocalDate appointmentDate
    );

        long countByDoctorIdAndAppointmentDate(
            Long doctorId,
            LocalDate appointmentDate
    );

        List<Appointment> findByPatient_Id(Long patientId);

        List<Appointment> findByDoctor_Id(Long doctorId);

        @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.appointmentStatus = 'HOLD'
        AND a.holdExpiredAt < :now
    """)
    List<Appointment> findExpiredAppointments(
            LocalDateTime now
    );
    List<Appointment> findByPatient_IdAndAppointmentStatusAndAppointmentDateGreaterThanEqualOrderByAppointmentDateAsc(
            Long patientId,
            AppointmentStatus status,
            LocalDate date
    );
    boolean existsByDoctor_Id(Long doctorId);
}