package com.local.PhongKham.chatbot.service;
import com.local.PhongKham.entity.Department;
import com.local.PhongKham.entity.Doctor;
import com.local.PhongKham.repository.DepartmentRepository;
import com.local.PhongKham.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class PromptBuilderService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;

    public String buildSystemPrompt() {

        List<Department> departments = departmentRepository.findAll();

        StringBuilder sb = new StringBuilder();

        sb.append("""
        Bạn là chatbot của Trung tâm y tế quận Thanh Xuân.

        Thông tin phòng khám:
        """);

        for (Department d : departments) {

            List<Doctor> doctors =
                    doctorRepository.findByDepartment_Id(d.getId());

            sb.append("- Chuyên khoa: ")
              .append(d.getName())
              .append("\n");

            if (!doctors.isEmpty()) {

                sb.append("  Bác sĩ: ");

                for (Doctor doc : doctors) {
                    sb.append(doc.getFullName())
                      .append(", ");
                }

                sb.append("\n");
            }
        }

        sb.append("""
        
        Giờ làm việc: 7h30 - 17h
        Số điện thoại phòng khám là: 0243.858.7155

        Nhiệm vụ:
        - Tư vấn chuyên khoa phù hợp
        - Gợi ý bác sĩ nếu có
        - Hướng dẫn đặt lịch
        - Trả lời ngắn gọn, lịch sự, bằng tiếng Việt
        """);

        return sb.toString();
    }
}
