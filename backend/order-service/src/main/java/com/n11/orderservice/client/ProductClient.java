package com.n11.orderservice.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.product-service.url}")
    private String productServiceUrl;

    public void decreaseStock(Long productId, int quantity) {
        log.info("Calling product-service to decrease stock. productId={}, quantity={}", productId, quantity);

        webClientBuilder.build()
                .patch()
                .uri(productServiceUrl + "/api/products/" + productId + "/decrease-stock?quantity=" + quantity)
                .retrieve()
                .toBodilessEntity()
                .block();

        log.info("Product stock decrease request completed. productId={}, quantity={}", productId, quantity);
    }
}