package com.n11.authservice.service;

import com.n11.authservice.entity.User;
import com.n11.authservice.enums.Role;
import com.n11.authservice.exception.AuthException;
import com.n11.authservice.publisher.EmailVerificationPublisher;
import com.n11.authservice.repository.UserRepository;
import com.n11.authservice.request.LoginRequest;
import com.n11.authservice.request.RegisterRequest;
import com.n11.authservice.request.VerifyEmailRequest;
import com.n11.authservice.response.AuthResponse;
import com.n11.authservice.response.RegisterResponse;
import com.n11.authservice.security.JwtService;
import com.n11.authservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailVerificationPublisher emailVerificationPublisher;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_WhenEmailNotExists_ShouldSaveUserAndPublishEvent() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Kazim Uzun");
        request.setEmail("kazim@test.com");
        request.setPassword("123456");

        User savedUser = User.builder()
                .id(1L)
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password("encoded-password")
                .role(Role.USER)
                .emailVerified(false)
                .verificationCode("123456")
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("Registration successful. Please verify your email.", response.getMessage());

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailVerificationPublisher, times(1)).publish(any());
    }

    @Test
    void register_WhenEmailExists_ShouldThrowAuthException() {
        RegisterRequest request = new RegisterRequest();
        request.setFullName("Kazim Uzun");
        request.setEmail("kazim@test.com");
        request.setPassword("123456");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class, () -> authService.register(request));

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verify(emailVerificationPublisher, never()).publish(any());
    }

    @Test
    void login_WhenCredentialsAreValidAndEmailVerified_ShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("kazim@test.com");
        request.setPassword("123456");

        User user = User.builder()
                .id(1L)
                .fullName("Kazim Uzun")
                .email(request.getEmail())
                .password("encoded-password")
                .role(Role.USER)
                .emailVerified(true)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user.getEmail())).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).matches(request.getPassword(), user.getPassword());
        verify(jwtService, times(1)).generateToken(user.getEmail());
    }

    @Test
    void login_WhenEmailNotVerified_ShouldThrowAuthException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("kazim@test.com");
        request.setPassword("123456");

        User user = User.builder()
                .id(1L)
                .fullName("Kazim Uzun")
                .email(request.getEmail())
                .password("encoded-password")
                .role(Role.USER)
                .emailVerified(false)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));

        assertEquals("Please verify your email first", exception.getMessage());

        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void verifyEmail_WhenCodeIsValid_ShouldVerifyUser() {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setEmail("kazim@test.com");
        request.setCode("123456");

        User user = User.builder()
                .id(1L)
                .fullName("Kazim Uzun")
                .email(request.getEmail())
                .password("encoded-password")
                .role(Role.USER)
                .emailVerified(false)
                .verificationCode("123456")
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse response = authService.verifyEmail(request);

        assertNotNull(response);
        assertEquals("Email verified successfully.", response.getMessage());
        assertTrue(user.isEmailVerified());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiresAt());

        verify(userRepository, times(1)).findByEmail(request.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void verifyEmail_WhenCodeIsInvalid_ShouldThrowAuthException() {
        VerifyEmailRequest request = new VerifyEmailRequest();
        request.setEmail("kazim@test.com");
        request.setCode("999999");

        User user = User.builder()
                .id(1L)
                .fullName("Kazim Uzun")
                .email(request.getEmail())
                .password("encoded-password")
                .role(Role.USER)
                .emailVerified(false)
                .verificationCode("123456")
                .verificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        AuthException exception = assertThrows(AuthException.class, () -> authService.verifyEmail(request));

        assertEquals("Invalid verification code", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }
}