package com.n11.orderservice.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateOrderItemRequest {

    @NotNull
    private Long productId;

    @NotBlank
    private String productName;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;
}