package com.local.PhongKham.chatbot.entity;

import com.local.PhongKham.chatbot.enums.BookingStep;
import com.local.PhongKham.chatbot.enums.ChatIntent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConversationSession {

    private String userId;

    // intent hiện tại
    private ChatIntent intent;

    // step hiện tại
    private BookingStep currentStep = BookingStep.NONE;

    // booking data
    private Long departmentId;

    private Long doctorId;

    private LocalDate appointmentDate;

    private String timeSlot;

    private String patientName;

    private String phone;

    // reset session
    public void reset() {

        this.intent = null;

        this.currentStep = BookingStep.NONE;

        this.departmentId = null;

        this.doctorId = null;

        this.appointmentDate = null;

        this.timeSlot = null;

        this.patientName = null;

        this.phone = null;
    }
}