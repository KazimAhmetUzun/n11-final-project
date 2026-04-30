package com.n11.authservice.controller;

import com.n11.authservice.request.LoginRequest;
import com.n11.authservice.request.RegisterRequest;
import com.n11.authservice.request.VerifyEmailRequest;
import com.n11.authservice.response.AuthResponse;
import com.n11.authservice.response.RegisterResponse;
import com.n11.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/verify-email")
    public RegisterResponse verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        return authService.verifyEmail(request);
    }
}