package com.n11.orderservice.service;


import com.n11.orderservice.request.CreateOrderRequest;
import com.n11.orderservice.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse create(CreateOrderRequest request);

    List<OrderResponse> getByUserEmail(String userEmail);

    OrderResponse getById(Long id);

    void updateStatusAfterPayment(Long orderId, boolean paymentSuccess);
}