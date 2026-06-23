package com.local.PhongKham.chatbot.entity;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.chatbot.enums.SenderType;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;
}