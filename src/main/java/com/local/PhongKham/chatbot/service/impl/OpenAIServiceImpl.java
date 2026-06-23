package com.local.PhongKham.chatbot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.local.PhongKham.chatbot.entity.ChatMessage;
import com.local.PhongKham.chatbot.enums.ChatIntent;
import com.local.PhongKham.chatbot.enums.SenderType;
import com.local.PhongKham.chatbot.service.ChatIntentService;
import com.local.PhongKham.chatbot.service.ChatMemoryService;
import com.local.PhongKham.chatbot.service.OpenAIService;
import com.local.PhongKham.chatbot.service.PromptBuilderService;
import com.local.PhongKham.dto.request.AppointmentRequest;
import com.local.PhongKham.dto.response.AppointmentResponse;
import com.local.PhongKham.dto.response.DoctorResponse;
import com.local.PhongKham.chatbot.dto.ChatAnalysisResponse;
import com.local.PhongKham.chatbot.dto.BookingContext;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.entity.Appointment;
import com.local.PhongKham.entity.Department;
import com.local.PhongKham.service.AppointmentService;
import com.local.PhongKham.service.DoctorService;
import com.local.PhongKham.service.PatientService;
import com.local.PhongKham.service.PaymentService;
import com.local.PhongKham.enums.AppointmentStatus;
import com.local.PhongKham.enums.Role;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.repository.AppointmentRepository;
import com.local.PhongKham.repository.DepartmentRepository;
import java.util.stream.Collectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OpenAIServiceImpl
        implements OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final ChatIntentService chatIntentService;

    private final AppointmentService appointmentService;

    private final PatientService patientService;

    private final DoctorService doctorService;

    private final PaymentService paymentService;

    private final AccountRepository accountRepository;

    private final DepartmentRepository departmentRepository;

    private final AppointmentRepository appointmentRepository;

    private final RestTemplate restTemplate =
            new RestTemplate();

    private final ObjectMapper mapper =
            new ObjectMapper();
    private final Map<Long, BookingContext>
            bookingSessions =
            new HashMap<>();

    private final ChatMemoryService
            chatMemoryService;

    private final PromptBuilderService promptBuilderService;

    public OpenAIServiceImpl(
                ChatMemoryService chatMemoryService,
                PromptBuilderService promptBuilderService,
                ChatIntentService chatIntentService,
                AppointmentService appointmentService,
                PatientService patientService,
                DoctorService doctorService,
                PaymentService paymentService,
                AccountRepository accountRepository,
                AppointmentRepository appointmentRepository,
                DepartmentRepository departmentRepository
        ) {

        this.chatMemoryService =
                chatMemoryService;

        this.promptBuilderService =
                promptBuilderService;

        this.chatIntentService =
                chatIntentService;

        this.appointmentService =
                appointmentService;

        this.patientService =
                patientService;

        this.doctorService =
                doctorService;

        this.paymentService =
                paymentService;

        this.accountRepository =
                accountRepository;
        this.departmentRepository =
                departmentRepository;
        this.appointmentRepository =
                appointmentRepository;
        }

    @Override
    public String ask(
            Long accountId,
            String message
    ) {
        BookingContext existingCtx =
        bookingSessions.get(accountId);

if (
        existingCtx != null
        &&
        (
                existingCtx.getDepartment() != null
                ||
                existingCtx.getAppointmentDate() != null
                ||
                existingCtx.getDoctor() != null
        )
) {

    ChatAnalysisResponse analysis =
            analyzeMessage(message);

    if (
            analysis != null
            &&
            analysis.getDepartment() != null
    ) {
        existingCtx.setDepartment(
                analysis.getDepartment()
        );
    }

    if (
            analysis != null
            &&
            analysis.getAppointmentDate() != null
    ) {
        existingCtx.setAppointmentDate(
                analysis.getAppointmentDate()
        );
    }

    if (
            analysis != null
            &&
            analysis.getDoctor() != null
    ) {
        existingCtx.setDoctor(
                analysis.getDoctor()
        );
    }

    Account account =
            accountRepository
                    .findById(accountId)
                    .orElseThrow();

    return processBooking(
            accountId,
            message,
            existingCtx,
            account
    );
}
        try {
        ChatAnalysisResponse analysis =
                analyzeMessage(
                        message
                );

        String intent =
                analysis != null
                        ? analysis.getIntent()
                        : null;
        System.out.println("INTENT = " + intent);
        System.out.println("DEPARTMENT = " + (analysis != null ? analysis.getDepartment() : null));
        System.out.println("DATE = " + (analysis != null ? analysis.getAppointmentDate() : null));
        System.out.println("DOCTOR = " + (analysis != null ? analysis.getDoctor() : null));
        BookingContext ctx =
                bookingSessions.getOrDefault(
                        accountId,
                        new BookingContext()
                );
        if (
                analysis != null
                &&
                analysis.getDepartment() != null
        ) {

        ctx.setDepartment(
                analysis.getDepartment()
        );
        }
        if (
        analysis != null &&
        analysis.getDoctor() != null
) {

    ctx.setDoctor(
            analysis.getDoctor()
    );
}

if (
        analysis != null &&
        analysis.getAppointmentDate() != null
) {

    ctx.setAppointmentDate(
            analysis.getAppointmentDate()
    );
}
        bookingSessions.put(
                accountId,
                ctx
        );
        System.out.println("===========");
        System.out.println("MESSAGE: " + message);
        System.out.println("INTENT: " + intent);
        System.out.println("===========");
        Account account =
                accountRepository
                        .findById(accountId)
                        .orElseThrow();

        Role role =
                account.getRole();
        if (
        "CHECK_MY_APPOINTMENTS"
                .equals(intent)
        ) {

        StringBuilder builder = new StringBuilder();

        // ================= PATIENT =================
        if (role == Role.PATIENT) {

                List<AppointmentResponse> appointments =
                        appointmentService.getMyAppointments(
                                account.getUsername()
                        );

                if (appointments.isEmpty()) {
                return "Bạn chưa có lịch khám nào.";
                }

                builder.append("Lịch khám của bạn:\n");

                for (AppointmentResponse a : appointments) {

                builder.append("- Bác sĩ: ")
                        .append(a.getDoctorName())
                        .append(" | Ngày khám: ")
                        .append(a.getAppointmentDate())
                        .append("\n");
                }

                return builder.toString();
        }
        // ================= ADMIN & RECEPTIONIST =================
        if (role == Role.ADMIN || role == Role.RECEPTIONIST) {
                LocalDate today = LocalDate.now();

                // 1. LỌC + SORT
                List<AppointmentResponse> appointments =
                        appointmentService.getAllAppointments()
                                .stream()
                                .filter(a ->
                                        a.getAppointmentStatus() == AppointmentStatus.CONFIRMED &&
                                        a.getAppointmentDate() != null &&
                                        !a.getAppointmentDate().isBefore(today)
                                )
                                .sorted(Comparator.comparing(AppointmentResponse::getAppointmentDate))
                                .toList();

                if (appointments.isEmpty()) {
                        return "Không có lịch khám đã thanh toán từ hôm nay.";
                }

                // 2. GROUP THEO NGÀY  (👉 CHÍNH LÀ ĐOẠN BẠN HỎI THÊM Ở ĐÂU)
                Map<LocalDate, List<AppointmentResponse>> grouped =
                        appointments.stream()
                                .collect(Collectors.groupingBy(
                                        AppointmentResponse::getAppointmentDate,
                                        TreeMap::new,
                                        Collectors.toList()
                                ));

                // 3. BUILD OUTPUT
                builder.append("📅 Danh sách lịch khám (đã thanh toán):\n\n");

                for (Map.Entry<LocalDate, List<AppointmentResponse>> entry : grouped.entrySet()) {

                builder.append("===== Ngày ")
                        .append(entry.getKey())
                        .append(" =====\n");

                for (AppointmentResponse a : entry.getValue()) {

                        builder.append("• ")
                                .append(a.getPatientName())
                                .append(" | BS: ")
                                .append(a.getDoctorName())
                                .append("\n");
                }

                builder.append("\n");
                }

                return builder.toString();
                }
        }
                
            List<Map<String, String>>
                    messages =
                    new ArrayList<>();

            messages.add(Map.of(
                "role",
                "system",

                "content",
                promptBuilderService.buildSystemPrompt()
                ));
            List<ChatMessage> history =
                    chatMemoryService
                            .getRecentMessages(
                                    accountId
                            );

            Collections.reverse(history);

            for (ChatMessage m : history) {

                messages.add(Map.of(
                        "role",
                        m.getSenderType() == SenderType.USER
                                ? "user"
                                : "assistant",

                        "content",
                        m.getContent()
                ));
                }
            messages.add(Map.of(
                    "role",
                    "user",

                    "content",
                    message
            ));

            Map<String, Object> body =
                    new HashMap<>();

            body.put(
                    "model",
                    "gpt-4.1-mini"
            );

            body.put(
                    "messages",
                    messages
            );

            HttpHeaders headers =
                    new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            headers.setBearerAuth(
                    apiKey
            );

            HttpEntity<Map<String, Object>>
                    entity =
                    new HttpEntity<>(
                            body,
                            headers
                    );

            ResponseEntity<String> response =
                    restTemplate.exchange(
                            "https://api.openai.com/v1/chat/completions",
                            HttpMethod.POST,
                            entity,
                            String.class
                    );

            JsonNode json =
                    mapper.readTree(
                            response.getBody()
                    );

            String answer =
                    json
                            .get("choices")
                            .get(0)
                            .get("message")
                            .get("content")
                            .asText();

            chatMemoryService.saveMessage(
                    accountId,
                    "user",
                    message
            );

            chatMemoryService.saveMessage(
                    accountId,
                    "assistant",
                    answer
            );

            return answer;

        } catch (Exception e) {

            e.printStackTrace();

            return "Chatbot đang bận.";
        }
    }
    private String reply(
                Long accountId,
                String userMessage,
                String assistantMessage
        ) {

        chatMemoryService.saveMessage(
                accountId,
                "user",
                userMessage
        );

        chatMemoryService.saveMessage(
                accountId,
                "assistant",
                assistantMessage
        );

        return assistantMessage;
    }
    private ChatAnalysisResponse analyzeMessage(
        String message
) {

    try {

        List<Map<String, String>> messages =
                new ArrayList<>();

        messages.add(
                Map.of(
                        "role",
                        "system",

                        "content",
                        """
                        Bạn là AI phân tích yêu cầu khám bệnh.

                        Hôm nay là %s.

                        Trả về JSON dạng:

                        {
                        \"intent\": \"...\",
                        \"department\": \"...\",
                        \"appointmentDate\": \"yyyy-MM-dd\",
                        \"doctor\": \"...\"
                        }

                        Intent có thể là:
                        - BOOK_APPOINTMENT
                        - CHECK_MY_APPOINTMENTS
                        - GENERAL_CHAT

                        Quy tắc:
                        - Nếu user muốn đặt lịch => BOOK_APPOINTMENT
                        - Nếu user hỏi lịch khám => CHECK_MY_APPOINTMENTS
                        - Nếu user nói triệu chứng thì tự suy luận chuyên khoa
                        - Nếu không có dữ liệu thì để null
                        - appointmentDate phải format yyyy-MM-dd
                        - "mai" = ngày tiếp theo
                        - "mốt" = sau 2 ngày
                        - phải tính ngày dựa trên ngày hiện tại
                        - Nếu user nhắc tên bác sĩ thì điền doctor
                        - Nếu không có thì doctor = null

                        Ví dụ:

                        User:
                        tôi đau mắt muốn khám mai bởi bác sĩ nguyễn văn A

                        Response:
                        {
                        \"intent\": \"BOOK_APPOINTMENT\",
                        \"department\": \"Mắt\",
                        \"appointmentDate\": \"2026-05-16\",
                        \"doctor\": \"nguyễn văn A\"
                        }

                        Chỉ trả JSON.
                        """
                        .formatted(LocalDate.now())
                )
        );

        messages.add(
                Map.of(
                        "role",
                        "user",

                        "content",
                        message
                )
        );

        Map<String, Object> body =
                new HashMap<>();

        body.put(
                "model",
                "gpt-4.1-mini"
        );

        body.put(
                "messages",
                messages
        );

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_JSON
        );

        headers.setBearerAuth(
                apiKey
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(
                        body,
                        headers
                );

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://api.openai.com/v1/chat/completions",
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        JsonNode json =
                mapper.readTree(
                        response.getBody()
                );

        String content =
                json
                        .get("choices")
                        .get(0)
                        .get("message")
                        .get("content")
                        .asText();
        content = content
        .replace("```json", "")
        .replace("```", "")
        .trim();

        System.out.println(
                "AI ANALYSIS: " + content
        );

        return mapper.readValue(
                content,
                ChatAnalysisResponse.class
        );

    } catch (Exception e) {

        e.printStackTrace();

        return null;
    }
}
private String processBooking(
        Long accountId,
        String message,
        BookingContext ctx,
        Account account
) {
        System.out.println("=== PROCESS BOOKING ===");
        System.out.println("CTX DEPARTMENT = " + ctx.getDepartment());
        System.out.println("CTX DATE = " + ctx.getAppointmentDate());
        System.out.println("CTX DOCTOR = " + ctx.getDoctor());
                Role role = account.getRole();

                if (role != Role.PATIENT) {

                        return reply(
                                accountId,
                                message,
                                "Chỉ bệnh nhân mới có thể đặt lịch khám."
                        );
                }

                try {

                        // ================= DEPARTMENT =================

                        String departmentName =
                                ctx.getDepartment();

                        if (
                                departmentName == null
                                ||
                                departmentName.isBlank()
                        ) {

                        List<Department> departments =
                                departmentRepository.findAll();

                        String departmentList =
                                departments.stream()
                                        .map(d -> "- " + d.getName())
                                        .collect(Collectors.joining("\n"));

                        return reply(
                                accountId,
                                message,
                                """
                                Bạn muốn khám chuyên khoa nào?

                                Các chuyên khoa hiện có:
                                %s
                                """
                                        .formatted(departmentList)
                        );
                        }

                        Department matchedDepartment =
                                departmentRepository
                                        .findAll()
                                        .stream()
                                        .filter(d ->

                                                d.getName()
                                                        .equalsIgnoreCase(
                                                                departmentName
                                                        )
                                        )
                                        .findFirst()
                                        .orElse(null);

                        if (matchedDepartment == null) {

                        return reply(
                                accountId,
                                message,
                                "Không tìm thấy chuyên khoa phù hợp."
                        );
                        }

                        // ================= DATE =================

                        LocalDate appointmentDate = null;

                        try {

                        if (
                                ctx.getAppointmentDate()
                                        != null
                        ) {

                        appointmentDate =
                                LocalDate.parse(
                                        ctx.getAppointmentDate()
                                );
                        }

                        } catch (Exception e) {

                        e.printStackTrace();
                        }

                        if (appointmentDate == null) {

                        return reply(
                                accountId,
                                message,
                                "Bạn muốn khám ngày nào?"
                        );
                        }
                        String doctorName = ctx.getDoctor();

                        if (
                                doctorName == null ||
                                doctorName.isBlank()
                        ) {

                        List<DoctorResponse> doctors =
                                doctorService.getAvailableDoctors(
                                        matchedDepartment.getId(),
                                        appointmentDate
                                );

                        if (doctors.isEmpty()) {

                                return reply(
                                        accountId,
                                        message,
                                        """
                                        Ngày %s hiện không còn
                                        bác sĩ trống của khoa %s.

                                        Vui lòng chọn ngày khác.
                                        """
                                                .formatted(
                                                        appointmentDate,
                                                        matchedDepartment.getName()
                                                )
                                );
                        }

                        String doctorList =
                                doctors.stream()
                                        .map(d ->
                                                "- " +
                                                d.getFullName()
                                        )
                                        .collect(Collectors.joining("\n"));

                        return reply(
                                accountId,
                                message,
                                """
                                Bạn muốn khám bác sĩ nào?

                                Danh sách bác sĩ còn lịch:

                                %s
                                """
                                        .formatted(doctorList)
                        );
                        }
                        List<DoctorResponse> doctors =
                                doctorService.getAvailableDoctors(
                                        matchedDepartment.getId(),
                                        appointmentDate
                                );

                        DoctorResponse selectedDoctor =
                                doctors.stream()
                                        .filter(d ->
                                                d.getFullName()
                                                .toLowerCase()
                                                .contains(
                                                doctorName.toLowerCase()
                                                )
                                        )
                                        .findFirst()
                                        .orElse(null);

                        if (selectedDoctor == null) {

                        return reply(
                                accountId,
                                message,
                                """
                                Bác sĩ %s không còn lịch
                                hoặc không thuộc chuyên khoa này.

                                Vui lòng chọn bác sĩ khác.
                                """
                                        .formatted(doctorName)
                        );
                        }

                        // ================= CREATE REQUEST =================

                        AppointmentRequest request =
                                new AppointmentRequest();

                        request.setDepartmentId(
                                matchedDepartment.getId()
                        );

                        request.setDoctorId(
                                selectedDoctor.getId()
                        );

                        request.setAppointmentDate(
                                appointmentDate
                        );

                        // ================= CREATE APPOINTMENT =================

                        AppointmentResponse response =
                                appointmentService.create(
                                        request
                                );
                        bookingSessions.remove(accountId);
                        return reply(
                                accountId,
                                message,
                                """
                                <div style='line-height:1.8'>
                                <b>Đặt lịch khám thành công.</b><br><br>

                                <b>Chuyên khoa:</b> %s<br>
                                <b>Ngày khám:</b> %s<br>
                                <b>Giờ khám:</b> %s - %s<br><br>

                                <a href="%s" target="_blank"
                                style="
                                        background:#1677ff;
                                        color:white;
                                        padding:10px 16px;
                                        border-radius:8px;
                                        text-decoration:none;
                                        display:inline-block;
                                        font-weight:bold;
                                ">
                                Thanh toán VNPay
                                </a>
                                </div>
                                """
                                        .formatted(
                                                matchedDepartment.getName(),
                                                response.getAppointmentDate(),
                                                response.getStartTime(),
                                                response.getEndTime(),
                                                response.getPaymentUrl()
                                        )
                        );

                } catch (Exception e) {

                        e.printStackTrace();

                        return reply(
                                accountId,
                                message,
                                "Không thể đặt lịch khám chuyên khoa này ngày hôm nay, vui lòng chọn ngày khác"
                        );
                }
                }
 // DÁN TOÀN BỘ CODE BOOK_APPOINTMENT VÀO ĐÂY
}