package com.local.PhongKham.chatbot.dto;

import lombok.Data;

@Data
public class ChatAnalysisResponse {

    private String intent;

    private String department;

    private String doctor;
    
    private String appointmentDate;
}