package com.n11.paymentservice.service;


import com.n11.paymentservice.request.PaymentRequest;
import com.n11.paymentservice.response.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse pay(PaymentRequest request);

    List<PaymentResponse> getByUserEmail(String userEmail);

    List<PaymentResponse> getByOrderId(Long orderId);
}