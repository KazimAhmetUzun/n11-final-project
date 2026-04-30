package com.n11.orderservice.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {

    @NotBlank
    private String userEmail;

    @Valid
    @NotEmpty
    private List<CreateOrderItemRequest> items;
}