package com.local.PhongKham.service;

import com.local.PhongKham.entity.Account;
import com.local.PhongKham.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User không tồn tại"));

                return new User(
                account.getUsername(),
                account.getPasswordHash(),
                List.of(
                        new SimpleGrantedAuthority(
                                account.getRole().name()
                        )
                )
        );
    }
}