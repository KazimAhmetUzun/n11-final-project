package com.n11.paymentservice.response;

import com.n11.paymentservice.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private String userEmail;
    private BigDecimal amount;
    private String transactionId;
    private PaymentStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
}