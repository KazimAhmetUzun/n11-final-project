package com.n11.paymentservice.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String userEmail;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @Valid
    @NotNull
    private PaymentCardRequest card;

    @Valid
    @NotNull
    private BuyerRequest buyer;
}