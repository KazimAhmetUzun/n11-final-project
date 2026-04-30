package com.n11.authservice.service;


import com.n11.authservice.request.LoginRequest;
import com.n11.authservice.request.RegisterRequest;
import com.n11.authservice.request.VerifyEmailRequest;
import com.n11.authservice.response.AuthResponse;
import com.n11.authservice.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    RegisterResponse verifyEmail(VerifyEmailRequest request);
}