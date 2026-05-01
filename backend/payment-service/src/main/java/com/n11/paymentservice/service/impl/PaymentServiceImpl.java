package com.n11.paymentservice.service.impl;

import com.iyzipay.model.Status;
import com.n11.paymentservice.client.IyzicoPaymentClient;
import com.n11.paymentservice.entity.Payment;
import com.n11.paymentservice.enums.PaymentStatus;
import com.n11.paymentservice.event.PaymentResultEvent;
import com.n11.paymentservice.publisher.PaymentResultPublisher;
import com.n11.paymentservice.repository.PaymentRepository;
import com.n11.paymentservice.request.PaymentRequest;
import com.n11.paymentservice.response.PaymentResponse;
import com.n11.paymentservice.service.PaymentService;
import com.n11.paymentservice.client.OrderClient;
import com.n11.paymentservice.client.OrderResponse;
import com.n11.paymentservice.exception.PaymentAmountMismatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentResultPublisher paymentResultPublisher;
    private final IyzicoPaymentClient iyzicoPaymentClient;
    private final OrderClient orderClient;

    @Override
    public PaymentResponse pay(PaymentRequest request) {
        log.info("Iyzico payment request received. orderId={}, userEmail={}, amount={}",
                request.getOrderId(), request.getUserEmail(), request.getAmount());

        OrderResponse order = orderClient.getOrderById(request.getOrderId());

        if (order == null) {
            throw new IllegalArgumentException("Order not found with id: " + request.getOrderId());
        }

        if (request.getAmount().compareTo(order.getTotalPrice()) != 0) {
            log.warn("Payment amount mismatch. orderId={}, requestAmount={}, orderAmount={}",
                    request.getOrderId(), request.getAmount(), order.getTotalPrice());

            throw new PaymentAmountMismatchException(
                    request.getOrderId(),
                    request.getAmount(),
                    order.getTotalPrice()
            );
        }

        com.iyzipay.model.Payment iyzicoPayment = iyzicoPaymentClient.createPayment(request);

        boolean success = Status.SUCCESS.getValue().equals(iyzicoPayment.getStatus());

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userEmail(request.getUserEmail())
                .amount(request.getAmount())
                .transactionId(iyzicoPayment.getPaymentId())
                .status(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED)
                .errorMessage(success ? null : iyzicoPayment.getErrorMessage())
                .createdAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        paymentResultPublisher.publish(new PaymentResultEvent(
                savedPayment.getOrderId(),
                savedPayment.getUserEmail(),
                savedPayment.getAmount(),
                savedPayment.getTransactionId(),
                savedPayment.getStatus(),
                savedPayment.getErrorMessage()
        ));

        if (success) {
            log.info("Iyzico payment completed successfully. paymentId={}, orderId={}, iyzicoPaymentId={}",
                    savedPayment.getId(), savedPayment.getOrderId(), savedPayment.getTransactionId());
        } else {
            log.warn("Iyzico payment failed. orderId={}, errorMessage={}",
                    request.getOrderId(), iyzicoPayment.getErrorMessage());
        }

        return toResponse(savedPayment);
    }

    @Override
    public List<PaymentResponse> getByUserEmail(String userEmail) {
        log.info("Fetching payments by userEmail={}", userEmail);

        List<PaymentResponse> payments = paymentRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Payments fetched successfully. userEmail={}, paymentCount={}", userEmail, payments.size());

        return payments;
    }

    @Override
    public List<PaymentResponse> getByOrderId(Long orderId) {
        log.info("Fetching payments by orderId={}", orderId);

        List<PaymentResponse> payments = paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Payments fetched successfully. orderId={}, paymentCount={}", orderId, payments.size());

        return payments;
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userEmail(payment.getUserEmail())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus())
                .errorMessage(payment.getErrorMessage())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}