package com.n11.paymentservice.event;

import com.n11.paymentservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultEvent {

    private Long orderId;
    private String userEmail;
    private BigDecimal amount;
    private String transactionId;
    private PaymentStatus status;
    private String errorMessage;
}