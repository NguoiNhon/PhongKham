package com.local.PhongKham.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class TimeSlot {

    private LocalTime start;

    private LocalTime end;
}