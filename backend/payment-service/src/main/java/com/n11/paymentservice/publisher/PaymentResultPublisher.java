package com.n11.paymentservice.publisher;


import com.n11.paymentservice.config.RabbitMQConfig;
import com.n11.paymentservice.event.PaymentResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResultPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(PaymentResultEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_RESULT_QUEUE, event);

        log.info("Payment result event published. orderId={}, status={}",
                event.getOrderId(), event.getStatus());
    }
}