package com.n11.paymentservice.exception;

import java.math.BigDecimal;

public class PaymentAmountMismatchException extends RuntimeException {

    public PaymentAmountMismatchException(Long orderId, BigDecimal requestAmount, BigDecimal orderAmount) {
        super("Payment amount does not match order total. orderId: " + orderId
                + ", requestAmount: " + requestAmount
                + ", orderAmount: " + orderAmount);
    }
}