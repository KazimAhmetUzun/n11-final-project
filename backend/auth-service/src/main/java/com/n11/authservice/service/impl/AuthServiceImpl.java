package com.n11.authservice.service.impl;

import com.n11.authservice.entity.User;
import com.n11.authservice.enums.Role;
import com.n11.authservice.event.EmailVerificationEvent;
import com.n11.authservice.exception.AuthException;
import com.n11.authservice.publisher.EmailVerificationPublisher;
import com.n11.authservice.repository.UserRepository;
import com.n11.authservice.request.LoginRequest;
import com.n11.authservice.request.RegisterRequest;
import com.n11.authservice.request.VerifyEmailRequest;
import com.n11.authservice.response.AuthResponse;
import com.n11.authservice.response.RegisterResponse;
import com.n11.authservice.security.JwtService;
import com.n11.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailVerificationPublisher emailVerificationPublisher;

    private String generateVerificationCode() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Register request received. email={}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Register failed. Email already exists. email={}", request.getEmail());
            throw new AuthException("Email already exists");
        }

        String verificationCode = generateVerificationCode();

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .emailVerified(false)
                .verificationCode(verificationCode)
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        User savedUser = userRepository.save(user);

        emailVerificationPublisher.publish(new EmailVerificationEvent(
                savedUser.getEmail(),
                savedUser.getFullName(),
                verificationCode
        ));

        log.info("User registered successfully. userId={}, email={}, emailVerified={}",
                savedUser.getId(), savedUser.getEmail(), savedUser.isEmailVerified());

        return new RegisterResponse("Registration successful. Please verify your email.");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login request received. email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed. User not found. email={}", request.getEmail());
                    return new AuthException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed. Invalid password. email={}", request.getEmail());
            throw new AuthException("Invalid password");
        }

        if (!user.isEmailVerified()) {
            log.warn("Login failed. Email not verified. email={}", request.getEmail());
            throw new AuthException("Please verify your email first");
        }

        String token = jwtService.generateToken(user.getEmail());

        log.info("User logged in successfully. userId={}, email={}", user.getId(), user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public RegisterResponse verifyEmail(VerifyEmailRequest request) {
        log.info("Email verification request received. email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.warn("Email verification failed. User not found. email={}", request.getEmail());
                    return new AuthException("User not found");
                });

        if (user.isEmailVerified()) {
            log.info("Email already verified. email={}", request.getEmail());
            return new RegisterResponse("Email is already verified.");
        }

        if (user.getVerificationCode() == null) {
            log.warn("Email verification failed. Verification code not found. email={}", request.getEmail());
            throw new AuthException("Verification code not found");
        }

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Email verification failed. Code expired. email={}", request.getEmail());
            throw new AuthException("Verification code expired");
        }

        if (!user.getVerificationCode().equals(request.getCode())) {
            log.warn("Email verification failed. Invalid code. email={}", request.getEmail());
            throw new AuthException("Invalid verification code");
        }

        user.setEmailVerified(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);

        userRepository.save(user);

        log.info("Email verified successfully. userId={}, email={}", user.getId(), user.getEmail());

        return new RegisterResponse("Email verified successfully.");
    }
}