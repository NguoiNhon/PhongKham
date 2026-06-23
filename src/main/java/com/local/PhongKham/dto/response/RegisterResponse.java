package com.local.PhongKham.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private String username;
    private String email;
    private String message;
}