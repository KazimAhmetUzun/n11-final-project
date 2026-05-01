package com.n11.paymentservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.order-service.url}")
    private String orderServiceUrl;

    public OrderResponse getOrderById(Long orderId) {
        log.info("Calling order-service to get order. orderId={}", orderId);

        return webClientBuilder.build()
                .get()
                .uri(orderServiceUrl + "/api/orders/" + orderId)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
    }
}