package com.n11.paymentservice.controller;

import com.n11.paymentservice.request.PaymentRequest;
import com.n11.paymentservice.response.PaymentResponse;
import com.n11.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse pay(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    @GetMapping("/user/{userEmail}")
    public List<PaymentResponse> getByUserEmail(@PathVariable String userEmail) {
        return paymentService.getByUserEmail(userEmail);
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentResponse> getByOrderId(@PathVariable Long orderId) {
        return paymentService.getByOrderId(orderId);
    }
}