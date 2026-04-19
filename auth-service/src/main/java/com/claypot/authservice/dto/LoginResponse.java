package com.claypot.authservice.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    public String token;

    public LoginResponse (String token) {
        this.token = token;
    }
}
