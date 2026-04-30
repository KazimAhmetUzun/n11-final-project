package com.n11.notificationservice.listener;

import com.n11.notificationservice.config.RabbitMQConfig;
import com.n11.notificationservice.event.EmailVerificationEvent;
import com.n11.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationListener {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_VERIFICATION_QUEUE)
    public void listen(EmailVerificationEvent event) {
        log.info("Email verification event received. email={}, fullName={}, code={}",
                event.getEmail(),
                event.getFullName(),
                event.getVerificationCode());

        emailService.sendVerificationEmail(event);
    }
}