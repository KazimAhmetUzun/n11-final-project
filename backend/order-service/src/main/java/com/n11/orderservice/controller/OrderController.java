package com.n11.orderservice.controller;

import com.n11.orderservice.request.CreateOrderRequest;
import com.n11.orderservice.response.OrderResponse;
import com.n11.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/user/{userEmail}")
    public List<OrderResponse> getByUserEmail(@PathVariable String userEmail) {
        return orderService.getByUserEmail(userEmail);
    }

    @GetMapping("/{id}")
    public OrderResponse getById(@PathVariable Long id) {
        return orderService.getById(id);
    }
}