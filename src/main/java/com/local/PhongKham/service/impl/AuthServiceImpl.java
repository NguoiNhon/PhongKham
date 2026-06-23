package com.local.PhongKham.service.impl;

import com.local.PhongKham.entity.Account;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.service.AuthService;
import com.local.PhongKham.util.JwtUtil;
import com.local.PhongKham.dto.request.ForgotPasswordRequest;
import com.local.PhongKham.dto.request.ResetPasswordRequest;
import com.local.PhongKham.entity.PasswordResetOtp;
import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.repository.PasswordResetOtpRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetOtpRepository
            passwordResetOtpRepository;
    private final JavaMailSender mailSender;

    @Override
    public String login(String username, String password) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User không tồn tại"));

        if (account.getStatus() == AccountStatus.INACTIVE) {
        throw new RuntimeException("Tài khoản chưa xác thực email");
        } else if (account.getStatus() == AccountStatus.BANNED) {
        throw new RuntimeException("Tài khoản của bạn đã bị khóa, vui lòng liên hệ admin để được hỗ trợ");
        }                

        // kiểm tra password
        boolean isMatch = passwordEncoder.matches(
                password,
                account.getPasswordHash()
        );

        if (!isMatch) {
            throw new RuntimeException("Sai mật khẩu");
        }

        // tạo JWT token
        return jwtUtil.generateToken(
                account.getUsername(),
                account.getRole().name()
        );
    }
    @Override
    @Transactional
    public String forgotPassword(
            ForgotPasswordRequest request
    ) {

        // check email tồn tại
        Account account =
                accountRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Email không tồn tại"
                                ));

        // xóa OTP cũ của email này
        passwordResetOtpRepository
                .deleteByEmail(request.getEmail());

        // generate otp bằng SecureRandom
        SecureRandom random = new SecureRandom();

        String otp = String.valueOf(
                100000 + random.nextInt(900000)
        );

        // lưu otp
        PasswordResetOtp passwordResetOtp =
                PasswordResetOtp.builder()
                        .email(
                                request.getEmail()
                        )
                        .otpCode(
                                otp
                        )
                        .expiredAt(
                                LocalDateTime.now()
                                        .plusMinutes(5)
                        )
                        .verified(false)
                        .build();

        passwordResetOtpRepository
                .save(passwordResetOtp);

        // gửi mail
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(
                request.getEmail()
        );

        message.setSubject(
                "OTP RESET PASSWORD"
        );

        message.setText(
                "Mã OTP của bạn là: "
                        + otp
                        + "\nOTP hết hạn sau 5 phút."
        );

        mailSender.send(message);

        return "Đã gửi OTP qua email";
    }

    @Override
    public String resetPassword(
            ResetPasswordRequest request
    ) {

        // tìm otp mới nhất
        PasswordResetOtp otpEntity =
                passwordResetOtpRepository
                        .findTopByEmailOrderByIdDesc(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "OTP không tồn tại"
                                ));

        // check otp
        if (!otpEntity.getOtpCode()
                .equals(request.getOtp())) {

            throw new RuntimeException(
                    "OTP không đúng"
            );
        }

        // check hết hạn
        if (otpEntity.getExpiredAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "OTP đã hết hạn"
            );
        }

        // check đã dùng chưa
        if (otpEntity.getVerified()) {

            throw new RuntimeException(
                    "OTP đã được sử dụng"
            );
        }

        // tìm account
        Account account =
                accountRepository
                        .findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account không tồn tại"
                                ));

        // update password
        account.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        accountRepository.save(account);

        // mark otp used
        otpEntity.setVerified(true);

        passwordResetOtpRepository
                .save(otpEntity);

        return "Đổi mật khẩu thành công";
    }
}