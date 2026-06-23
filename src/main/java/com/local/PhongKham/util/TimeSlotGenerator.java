package com.local.PhongKham.util;

import com.local.PhongKham.entity.TimeSlotRule;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeSlotGenerator {

    public List<TimeSlot> generateSlots(
            TimeSlotRule rule
    ) {

        List<TimeSlot> slots = new ArrayList<>();

        int duration = rule.getSlotDuration();

        LocalTime current =
                rule.getWorkingStart();

        while (
                current.plusMinutes(duration)
                        .compareTo(rule.getWorkingEnd()) <= 0
        ) {

            LocalTime slotEnd =
                    current.plusMinutes(duration);

            // Bỏ qua giờ nghỉ trưa
            boolean overlapLunch =
                    current.isBefore(rule.getLunchEnd())
                    &&
                    slotEnd.isAfter(rule.getLunchStart());

            if (!overlapLunch) {

                slots.add(
                        new TimeSlot(
                                current,
                                slotEnd
                        )
                );
            }

            current = slotEnd;
        }

        return slots;
    }
}