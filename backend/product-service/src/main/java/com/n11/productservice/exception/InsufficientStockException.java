package com.n11.productservice.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId, int requestedQuantity, int availableStock) {
        super("Insufficient stock for product id: " + productId
                + ". Requested: " + requestedQuantity
                + ", Available: " + availableStock);
    }
}