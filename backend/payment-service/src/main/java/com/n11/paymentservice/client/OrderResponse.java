package com.n11.paymentservice.client;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderResponse {

    private Long id;
    private String userEmail;
    private String status;
    private BigDecimal totalPrice;
}