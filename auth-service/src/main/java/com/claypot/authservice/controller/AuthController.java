package com.claypot.authservice.controller;

import com.claypot.authservice.dto.ApiResponse;
import com.claypot.authservice.dto.LoginRequest;
import com.claypot.authservice.dto.LoginResponse;
import com.claypot.authservice.dto.SignupRequest;
import com.claypot.authservice.entity.AppUser;
import com.claypot.authservice.repository.AppUserRepository;
import com.claypot.authservice.security.JwtUtil;
import com.claypot.authservice.service.AuthService;
import com.claypot.authservice.utility.SignupResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppUserRepository appUserRepository;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(
            @RequestBody SignupRequest signupRequest) {
        SignupResult signupResult = authService.signUp(signupRequest);
        ResponseEntity<ApiResponse<Void>> response = null;
        switch (signupResult) {
            case EMAIL_ALREADY_EXISTS:
                response = ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(
                                false,
                                "Email already exists",
                                null
                        ));
                break;

            case USERNAME_ALREADY_EXISTS:
                response =  ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(
                                false,
                                "Username already exists",
                                null
                        ));
                break;

            case SUCCESS:
                response = ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse<>(
                                true,
                                "User registered successfully",
                                null
                        ));
                break;
        }
        return response;

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        Optional<AppUser> user = appUserRepository.findByEmail(loginRequest.email);

        if(user.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(
                            false,
                            "User not found",
                            null));
        }

        boolean isPasswordMatch = authService
                .checkPassword(loginRequest.password, user.get().getPassword());

        if (!isPasswordMatch) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(
                            false,
                            "Invalid credentials",
                            null));
        }

        String token = JwtUtil.generateToken(user.get().getId(), user.get().getUsername());
        return ResponseEntity.ok()
                .body(new ApiResponse<>(
                        true,
                        "Login successful",
                        new LoginResponse(token)));
    }
}
