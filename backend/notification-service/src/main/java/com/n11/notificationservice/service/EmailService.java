package com.n11.notificationservice.service;


import com.n11.notificationservice.event.EmailVerificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(EmailVerificationEvent event) {
        log.info("Sending verification email. email={}", event.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getEmail());
        message.setSubject("E-Commerce Email Verification Code");
        message.setText(buildEmailBody(event));

        javaMailSender.send(message);

        log.info("Verification email sent successfully. email={}", event.getEmail());
    }

    private String buildEmailBody(EmailVerificationEvent event) {
        return "Hello " + event.getFullName() + ",\n\n"
                + "Your email verification code is: " + event.getVerificationCode() + "\n\n"
                + "This code is valid for 10 minutes.\n\n"
                + "Thank you.";
    }
}