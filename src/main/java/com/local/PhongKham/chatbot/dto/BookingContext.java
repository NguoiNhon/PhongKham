package com.local.PhongKham.chatbot.dto;

public class BookingContext {

    private String department;

    private String appointmentDate;

    private String doctor;

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(
            String department
    ) {
        this.department = department;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(
            String appointmentDate
    ) {
        this.appointmentDate =
                appointmentDate;
    }
}