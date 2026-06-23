package com.local.PhongKham.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import com.local.PhongKham.entity.EmailVerificationOtp;
import com.local.PhongKham.repository.EmailVerificationOtpRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.local.PhongKham.dto.response.AccountResponse;
import com.local.PhongKham.entity.Account;
import com.local.PhongKham.entity.Patient;
import com.local.PhongKham.enums.AccountStatus;
import com.local.PhongKham.enums.Role;
import com.local.PhongKham.repository.AccountRepository;
import com.local.PhongKham.repository.PatientRepository;
import com.local.PhongKham.service.AccountService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final EmailVerificationOtpRepository otpRepository;
    private final JavaMailSender mailSender;

    @Override
    public Account register(
        String username,
        String email,
        String password,
        String fullName,
        LocalDate dateOfBirth,
        String gender,
        String phone
        ) {

        if (accountRepository.existsByUsername(username)) {
            throw new RuntimeException("Username đã tồn tại");
        }

        Account existingAccount =
                accountRepository
                        .findByEmail(email)
                        .orElse(null);

        if (existingAccount != null) {

        // đã xác thực thật rồi
        if (existingAccount.getStatus()
                == AccountStatus.ACTIVE) {

                throw new RuntimeException(
                        "Email đã tồn tại"
                );
        }

        // chưa xác thực -> gửi lại OTP
        resendVerifyOtp(email);

        throw new RuntimeException(
                "Email chưa xác thực. Đã gửi lại OTP."
        );
        }

        Account account = Account.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(Role.PATIENT)
                .status(AccountStatus.INACTIVE)
                .build();

        Account savedAccount =
                accountRepository.save(account);
        Patient patient = Patient.builder()
                .fullName(fullName)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .phone(phone)
                .account(savedAccount)
                .build();

        patientRepository.save(patient);
        otpRepository.deleteByEmail(email);

        String otp = String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 999999)
        );

        EmailVerificationOtp verificationOtp =
                EmailVerificationOtp.builder()
                        .email(email)
                        .otpCode(otp)
                        .expiredAt(LocalDateTime.now().plusMinutes(5))
                        .verified(false)
                        .build();

        otpRepository.save(verificationOtp);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Xác thực email");

        message.setText(
                "Mã OTP xác thực tài khoản của bạn là: "
                        + otp
        );

        mailSender.send(message);
                return savedAccount;
        }
    @Override
    public AccountResponse getMyInfo(String username) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        return AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .role(account.getRole())
                .status(account.getStatus())
                .build();
    }
    @Override
    public void changePassword(
            String username,
            String oldPassword,
            String newPassword
    ) {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));

        boolean isMatch = passwordEncoder.matches(
                oldPassword,
                account.getPasswordHash()
        );

        if (!isMatch) {
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        account.setPasswordHash(
                passwordEncoder.encode(newPassword)
        );

        accountRepository.save(account);
    }
    @Override
        public List<AccountResponse> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(account -> AccountResponse.builder()
                        .id(account.getId())
                        .username(account.getUsername())
                        .email(account.getEmail())
                        .role(account.getRole())
                        .status(account.getStatus())
                        .build())
                .toList();
        }
        @Override
        public AccountResponse getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));

        return AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .role(account.getRole())
                .status(account.getStatus())
                .build();
        }
        @Override
        public AccountResponse createAccountByAdmin(
                String username,
                String email,
                String password,
                Role role
        ) {

        if (accountRepository.existsByUsername(username)) {
                throw new RuntimeException("Username đã tồn tại");
        }

        if (accountRepository.existsByEmail(email)) {
                throw new RuntimeException("Email đã tồn tại");
        }

        Account account = Account.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .status(AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(account);

        return AccountResponse.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .role(saved.getRole())
                .status(saved.getStatus())
                .build();
        }
        @Override
        public AccountResponse updateAccount(
                Long id,
                String email,
                Role role,
                AccountStatus status
        ) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));

        account.setEmail(email);
        account.setRole(role);
        account.setStatus(status);

        Account updated = accountRepository.save(account);

        return AccountResponse.builder()
                .id(updated.getId())
                .username(updated.getUsername())
                .email(updated.getEmail())
                .role(updated.getRole())
                .status(updated.getStatus())
                .build();
        }
        @Override
        public void deactivateAccount(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy account"));

        account.setStatus(AccountStatus.BANNED);

        accountRepository.save(account);
        }
        @Override
        public void verifyEmail(String email, String otp) {

        EmailVerificationOtp otpEntity =
                otpRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy OTP"));

        if (otpEntity.isVerified()) {
                throw new RuntimeException("OTP đã được sử dụng");
        }

        // check otp trước
        if (!otpEntity.getOtpCode().equals(otp)) {
                throw new RuntimeException("OTP không đúng");
        }

        // check hết hạn
        if (LocalDateTime.now()
                .isAfter(otpEntity.getExpiredAt())) {

                throw new RuntimeException(
                        "OTP đã hết hạn, vui lòng gửi lại mã mới"
                );
        }

        otpEntity.setVerified(true);

        Account account = accountRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy account"));

        account.setStatus(AccountStatus.ACTIVE);

        otpRepository.save(otpEntity);
        accountRepository.save(account);
        }
        @Override
        @Transactional
        public void resendVerifyOtp(String email) {

        Account account = accountRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy account"));

        // nếu đã active thì khỏi gửi nữa
        if (account.getStatus() == AccountStatus.ACTIVE) {
                throw new RuntimeException(
                        "Tài khoản đã xác thực"
                );
        }

        // xóa otp cũ
        otpRepository.deleteByEmail(email);

        // tạo otp mới
        String otp = String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100000, 999999)
        );

        EmailVerificationOtp verificationOtp =
                EmailVerificationOtp.builder()
                        .email(email)
                        .otpCode(otp)
                        .expiredAt(
                                LocalDateTime.now()
                                        .plusMinutes(5)
                        )
                        .verified(false)
                        .build();

        otpRepository.save(verificationOtp);

        // gửi mail
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Gửi lại mã xác thực"
        );

        message.setText(
                "Mã OTP mới của bạn là: " + otp
                        + "\nOTP hết hạn sau 5 phút."
        );

        mailSender.send(message);
        }
        @Override
        public void activateAccount(Long id) {

        Account account =
                accountRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Account not found"
                                ));

        account.setStatus(
                AccountStatus.ACTIVE
        );

        accountRepository.save(account);
        }
}