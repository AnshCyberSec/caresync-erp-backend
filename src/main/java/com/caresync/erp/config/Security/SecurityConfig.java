package com.caresync.erp.config.Security;

import java.util.Arrays;
import org.springframework.security.config.Customizer;
import com.caresync.erp.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // ==================== PUBLIC ENDPOINTS ====================
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/patient/register").permitAll()
                        .requestMatchers("/api/patient/login").permitAll()
                        .requestMatchers("/api/doctors/public-register").permitAll()
                        .requestMatchers("/api/auth/public-register/receptionist").permitAll()
                        .requestMatchers("/error").permitAll()

                        // ==================== PATIENT SELF-SERVICE ENDPOINTS ====================
                        .requestMatchers("/api/patient/dashboard/**").hasRole("PATIENT")
                        .requestMatchers("/api/patient/appointments/**").hasRole("PATIENT")
                        .requestMatchers("/api/patient/profile/**").hasRole("PATIENT")
                        .requestMatchers("/api/patient/doctors").hasRole("PATIENT")
                        .requestMatchers("/api/patient/appointments/book").hasRole("PATIENT")
                        .requestMatchers("/api/patient/**").hasRole("PATIENT")

                        // ==================== RECEPTIONIST MANAGEMENT (ADMIN ONLY) ====================
                        .requestMatchers("/api/receptionists/**").hasRole("ADMIN")

                        // ==================== RECEPTIONIST ENDPOINTS  ====================

                        .requestMatchers("/api/receptionist/**").hasAnyRole("RECEPTIONIST", "ADMIN")

                        // ==================== DOCTORS ENDPOINTS - PATIENT CAN VIEW ====================
                        .requestMatchers("/api/doctors/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "PATIENT")
                        .requestMatchers("/api/doctors/{doctorId}/available-slots").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "PATIENT")

                        // ==================== APPOINTMENT ENDPOINTS ====================
                        .requestMatchers("/api/appointments/patient/**").hasAnyRole("PATIENT", "ADMIN", "RECEPTIONIST", "DOCTOR")
                        .requestMatchers("/api/appointments/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR")

                        // ==================== ADMIN/RECEPTIONIST/DOCTOR ENDPOINTS ====================
                        .requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR")
                        .requestMatchers("/api/dashboard/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/doctor/**").hasRole("DOCTOR")
                        .requestMatchers("/api/dashboard/receptionist/**").hasRole("RECEPTIONIST")
                        .requestMatchers("/api/reports/**").hasRole("ADMIN")

                        .anyRequest().authenticated()

                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }


}
