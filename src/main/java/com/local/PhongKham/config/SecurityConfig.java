package com.local.PhongKham.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                    // public
                    .requestMatchers(
                            "/api/auth/**",
                            "/api/accounts/**",
                            "/api/doctors/**",
                            "/api/patients/**",
                            "/api/admins/**",
                            "/api/appointments/**",
                            "/api/payment/**",
                            "/*.html",
                            "/css/**",
                            "/js/**",
                            "/api/departments/**",
                            "/api/time-slot-rules/**",
                            "/images/**",
                            "/api/chat/**",
                            "/uploads/**",
                            "/api/notifications/**"
                    ).permitAll()
                    // ADMIN
                    // .requestMatchers(
                    //         )
                    // .hasAuthority("ADMIN")
                    // DOCTOR
                    //.requestMatchers("/api/doctors/**")
                    //.hasAnyAuthority("DOCTOR", "ADMIN")
                    // PATIENT
                    // .requestMatchers("/api/patients/**")
                    // .hasAuthority("PATIENT")
                    .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}