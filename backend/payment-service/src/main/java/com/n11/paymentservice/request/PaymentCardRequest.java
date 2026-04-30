package com.n11.paymentservice.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCardRequest {

    @NotBlank
    private String cardHolderName;

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String expireMonth;

    @NotBlank
    private String expireYear;

    @NotBlank
    private String cvc;
}