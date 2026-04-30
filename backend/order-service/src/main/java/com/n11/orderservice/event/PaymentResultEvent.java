package com.n11.orderservice.event;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentResultEvent {

    private Long orderId;
    private String userEmail;
    private BigDecimal amount;
    private String transactionId;
    private PaymentStatus status;
    private String errorMessage;

    public enum PaymentStatus {
        SUCCESS,
        FAILED
    }
}