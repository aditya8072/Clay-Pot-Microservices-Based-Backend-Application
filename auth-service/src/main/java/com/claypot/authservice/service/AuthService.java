package com.claypot.authservice.service;

import com.claypot.authservice.dto.SignupRequest;
import com.claypot.authservice.entity.AppUser;
import com.claypot.authservice.repository.AppUserRepository;
import com.claypot.authservice.utility.SignupResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResult signUp(SignupRequest signupRequest) {
        boolean emailExists = appUserRepository.findByEmail(signupRequest.getEmail()).isPresent();
        if (appUserRepository.existsByEmail(signupRequest.getEmail())) {
            return SignupResult.EMAIL_ALREADY_EXISTS;
        }

        if (appUserRepository.existsByUsername(signupRequest.getUsername())) {
            return SignupResult.USERNAME_ALREADY_EXISTS;
        }
        AppUser appUser = AppUser.builder()
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();
        appUserRepository.save(appUser);
        return SignupResult.SUCCESS;
    }

    public boolean checkPassword(String loginRequestPassword, String userPassword) {
        return passwordEncoder.matches(loginRequestPassword, userPassword);
    }
}
