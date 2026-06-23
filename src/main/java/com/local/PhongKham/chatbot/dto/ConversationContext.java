package com.local.PhongKham.chatbot.dto;
import com.local.PhongKham.chatbot.enums.ChatIntent;
import com.local.PhongKham.chatbot.enums.BookingStep;


import java.time.LocalDate;

public class ConversationContext {

    private ChatIntent intent;

    private Long departmentId;

    private Long doctorId;

    private LocalDate appointmentDate;

    private String timeSlot;

    private BookingStep currentStep;

    public ConversationContext() {
    }

    public ChatIntent getIntent() {
        return intent;
    }

    public void setIntent(ChatIntent intent) {
        this.intent = intent;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public BookingStep getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(BookingStep currentStep) {
        this.currentStep = currentStep;
    }
}