package com.n11.orderservice.listener;


import com.n11.orderservice.config.RabbitMQConfig;
import com.n11.orderservice.event.PaymentResultEvent;
import com.n11.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResultListener {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_RESULT_QUEUE)
    public void listen(PaymentResultEvent event) {
        log.info("Payment result event received. orderId={}, status={}, transactionId={}",
                event.getOrderId(), event.getStatus(), event.getTransactionId());

        boolean paymentSuccess = event.getStatus() == PaymentResultEvent.PaymentStatus.SUCCESS;

        orderService.updateStatusAfterPayment(event.getOrderId(), paymentSuccess);
    }
}