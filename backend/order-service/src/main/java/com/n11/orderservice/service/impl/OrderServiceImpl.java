package com.n11.orderservice.service.impl;

import com.n11.orderservice.entity.Order;
import com.n11.orderservice.entity.OrderItem;
import com.n11.orderservice.enums.OrderStatus;
import com.n11.orderservice.exception.OrderNotFoundException;
import com.n11.orderservice.repository.OrderRepository;
import com.n11.orderservice.request.CreateOrderItemRequest;
import com.n11.orderservice.request.CreateOrderRequest;
import com.n11.orderservice.response.OrderItemResponse;
import com.n11.orderservice.response.OrderResponse;
import com.n11.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResponse create(CreateOrderRequest request) {
        log.info("Creating order. userEmail={}, itemCount={}",
                request.getUserEmail(), request.getItems().size());

        Order order = Order.builder()
                .userEmail(request.getUserEmail())
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        List<OrderItem> items = request.getItems()
                .stream()
                .map(itemRequest -> toOrderItem(itemRequest, order))
                .toList();

        BigDecimal totalPrice = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setItems(items);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully. orderId={}, userEmail={}, totalPrice={}",
                savedOrder.getId(), savedOrder.getUserEmail(), savedOrder.getTotalPrice());

        return toResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getByUserEmail(String userEmail) {
        log.info("Fetching orders by userEmail={}", userEmail);

        List<OrderResponse> orders = orderRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Orders fetched successfully. userEmail={}, orderCount={}", userEmail, orders.size());

        return orders;
    }

    @Override
    public OrderResponse getById(Long id) {
        log.info("Fetching order by id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found. orderId={}", id);
                    return new OrderNotFoundException(id);
                });

        log.info("Order fetched successfully. orderId={}, userEmail={}, status={}",
                order.getId(), order.getUserEmail(), order.getStatus());

        return toResponse(order);
    }

    private OrderItem toOrderItem(CreateOrderItemRequest request, Order order) {
        BigDecimal totalPrice = request.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        return OrderItem.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .order(order)
                .build();
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userEmail(order.getUserEmail())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(order.getItems()
                        .stream()
                        .map(this::toItemResponse)
                        .toList())
                .build();
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }

    @Override
    public void updateStatusAfterPayment(Long orderId, boolean paymentSuccess) {
        log.info("Updating order status after payment. orderId={}, paymentSuccess={}",
                orderId, paymentSuccess);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order status update failed. Order not found. orderId={}", orderId);
                    return new OrderNotFoundException(orderId);
                });

        order.setStatus(paymentSuccess ? OrderStatus.PAID : OrderStatus.CANCELLED);

        orderRepository.save(order);

        log.info("Order status updated after payment. orderId={}, newStatus={}",
                order.getId(), order.getStatus());
    }
}