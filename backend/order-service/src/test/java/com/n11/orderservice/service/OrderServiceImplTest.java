package com.n11.orderservice.service;

import com.n11.orderservice.entity.Order;
import com.n11.orderservice.entity.OrderItem;
import com.n11.orderservice.enums.OrderStatus;
import com.n11.orderservice.exception.OrderNotFoundException;
import com.n11.orderservice.repository.OrderRepository;
import com.n11.orderservice.request.CreateOrderItemRequest;
import com.n11.orderservice.request.CreateOrderRequest;
import com.n11.orderservice.response.OrderResponse;
import com.n11.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void create_ShouldCreateOrderWithItemsAndTotalPrice() {
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setProductName("iPhone 15");
        itemRequest.setPrice(BigDecimal.valueOf(10.00));
        itemRequest.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserEmail("kazim@test.com");
        request.setItems(List.of(itemRequest));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);

            order.getItems().forEach(item -> item.setId(1L));

            return order;
        });

        OrderResponse response = orderService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("kazim@test.com", response.getUserEmail());
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(BigDecimal.valueOf(20.00), response.getTotalPrice());
        assertEquals(1, response.getItems().size());
        assertEquals("iPhone 15", response.getItems().get(0).getProductName());
        assertEquals(BigDecimal.valueOf(20.00), response.getItems().get(0).getTotalPrice());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getByUserEmail_ShouldReturnOrders() {
        Order order = createOrder();

        when(orderRepository.findByUserEmail("kazim@test.com")).thenReturn(List.of(order));

        List<OrderResponse> response = orderService.getByUserEmail("kazim@test.com");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
        assertEquals("kazim@test.com", response.get(0).getUserEmail());

        verify(orderRepository, times(1)).findByUserEmail("kazim@test.com");
    }

    @Test
    void getById_WhenOrderExists_ShouldReturnOrder() {
        Order order = createOrder();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(OrderStatus.CREATED, response.getStatus());

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getById_WhenOrderDoesNotExist_ShouldThrowException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getById(99L));

        verify(orderRepository, times(1)).findById(99L);
    }

    @Test
    void updateStatusAfterPayment_WhenPaymentSuccess_ShouldSetStatusPaid() {
        Order order = createOrder();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.updateStatusAfterPayment(1L, true);

        assertEquals(OrderStatus.PAID, order.getStatus());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateStatusAfterPayment_WhenPaymentFailed_ShouldSetStatusCancelled() {
        Order order = createOrder();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderService.updateStatusAfterPayment(1L, false);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateStatusAfterPayment_WhenOrderDoesNotExist_ShouldThrowException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.updateStatusAfterPayment(99L, true));

        verify(orderRepository, times(1)).findById(99L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    private Order createOrder() {
        Order order = Order.builder()
                .id(1L)
                .userEmail("kazim@test.com")
                .totalPrice(BigDecimal.valueOf(20.00))
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        OrderItem item = OrderItem.builder()
                .id(1L)
                .productId(1L)
                .productName("iPhone 15")
                .price(BigDecimal.valueOf(10.00))
                .quantity(2)
                .totalPrice(BigDecimal.valueOf(20.00))
                .order(order)
                .build();

        order.setItems(List.of(item));

        return order;
    }
}