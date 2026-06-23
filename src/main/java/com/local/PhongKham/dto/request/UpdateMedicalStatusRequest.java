package com.local.PhongKham.dto.request;

import com.local.PhongKham.enums.MedicalStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMedicalStatusRequest {

    private MedicalStatus medicalStatus;
}