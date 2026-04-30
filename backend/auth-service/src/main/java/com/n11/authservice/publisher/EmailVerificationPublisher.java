package com.n11.authservice.publisher;


import com.n11.authservice.config.RabbitMQConfig;
import com.n11.authservice.event.EmailVerificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(EmailVerificationEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_VERIFICATION_QUEUE, event);

        log.info("Email verification event published. email={}", event.getEmail());
    }
}