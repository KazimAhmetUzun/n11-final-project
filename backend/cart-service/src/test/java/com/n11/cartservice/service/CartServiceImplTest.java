package com.n11.cartservice.service;

import com.n11.cartservice.entity.CartItem;
import com.n11.cartservice.exception.CartItemNotFoundException;
import com.n11.cartservice.repository.CartItemRepository;
import com.n11.cartservice.request.AddCartItemRequest;
import com.n11.cartservice.request.UpdateCartItemRequest;
import com.n11.cartservice.response.CartItemResponse;
import com.n11.cartservice.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void addItem_WhenItemDoesNotExist_ShouldCreateNewCartItem() {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setUserEmail("kazim@test.com");
        request.setProductId(1L);
        request.setProductName("iPhone 15");
        request.setPrice(BigDecimal.valueOf(10.00));
        request.setQuantity(1);

        CartItem savedItem = CartItem.builder()
                .id(1L)
                .userEmail(request.getUserEmail())
                .productId(request.getProductId())
                .productName(request.getProductName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .build();

        when(cartItemRepository.findByUserEmailAndProductId(request.getUserEmail(), request.getProductId()))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedItem);

        CartItemResponse response = cartService.addItem(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("kazim@test.com", response.getUserEmail());
        assertEquals(1L, response.getProductId());
        assertEquals(1, response.getQuantity());
        assertEquals(BigDecimal.valueOf(10.00), response.getTotalPrice());

        verify(cartItemRepository, times(1))
                .findByUserEmailAndProductId(request.getUserEmail(), request.getProductId());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addItem_WhenItemExists_ShouldIncreaseQuantity() {
        AddCartItemRequest request = new AddCartItemRequest();
        request.setUserEmail("kazim@test.com");
        request.setProductId(1L);
        request.setProductName("iPhone 15");
        request.setPrice(BigDecimal.valueOf(10.00));
        request.setQuantity(2);

        CartItem existingItem = CartItem.builder()
                .id(1L)
                .userEmail(request.getUserEmail())
                .productId(request.getProductId())
                .productName(request.getProductName())
                .price(request.getPrice())
                .quantity(1)
                .build();

        CartItem savedItem = CartItem.builder()
                .id(1L)
                .userEmail(request.getUserEmail())
                .productId(request.getProductId())
                .productName(request.getProductName())
                .price(request.getPrice())
                .quantity(3)
                .build();

        when(cartItemRepository.findByUserEmailAndProductId(request.getUserEmail(), request.getProductId()))
                .thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(savedItem);

        CartItemResponse response = cartService.addItem(request);

        assertNotNull(response);
        assertEquals(3, response.getQuantity());
        assertEquals(BigDecimal.valueOf(30.00), response.getTotalPrice());

        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void getCart_ShouldReturnUserCartItems() {
        CartItem item = CartItem.builder()
                .id(1L)
                .userEmail("kazim@test.com")
                .productId(1L)
                .productName("iPhone 15")
                .price(BigDecimal.valueOf(10.00))
                .quantity(2)
                .build();

        when(cartItemRepository.findByUserEmail("kazim@test.com"))
                .thenReturn(List.of(item));

        List<CartItemResponse> response = cartService.getCart("kazim@test.com");

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("iPhone 15", response.get(0).getProductName());
        assertEquals(BigDecimal.valueOf(20.00), response.get(0).getTotalPrice());

        verify(cartItemRepository, times(1)).findByUserEmail("kazim@test.com");
    }

    @Test
    void updateItem_WhenItemExists_ShouldUpdateQuantity() {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(5);

        CartItem existingItem = CartItem.builder()
                .id(1L)
                .userEmail("kazim@test.com")
                .productId(1L)
                .productName("iPhone 15")
                .price(BigDecimal.valueOf(10.00))
                .quantity(2)
                .build();

        CartItem updatedItem = CartItem.builder()
                .id(1L)
                .userEmail("kazim@test.com")
                .productId(1L)
                .productName("iPhone 15")
                .price(BigDecimal.valueOf(10.00))
                .quantity(5)
                .build();

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(existingItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(updatedItem);

        CartItemResponse response = cartService.updateItem(1L, request);

        assertNotNull(response);
        assertEquals(5, response.getQuantity());
        assertEquals(BigDecimal.valueOf(50.00), response.getTotalPrice());

        verify(cartItemRepository, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).save(existingItem);
    }

    @Test
    void updateItem_WhenItemDoesNotExist_ShouldThrowException() {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.setQuantity(5);

        when(cartItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class, () -> cartService.updateItem(99L, request));

        verify(cartItemRepository, times(1)).findById(99L);
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void removeItem_WhenItemExists_ShouldDeleteItem() {
        when(cartItemRepository.existsById(1L)).thenReturn(true);

        cartService.removeItem(1L);

        verify(cartItemRepository, times(1)).existsById(1L);
        verify(cartItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void removeItem_WhenItemDoesNotExist_ShouldThrowException() {
        when(cartItemRepository.existsById(99L)).thenReturn(false);

        assertThrows(CartItemNotFoundException.class, () -> cartService.removeItem(99L));

        verify(cartItemRepository, times(1)).existsById(99L);
        verify(cartItemRepository, never()).deleteById(anyLong());
    }

    @Test
    void clearCart_ShouldDeleteAllItemsByUserEmail() {
        cartService.clearCart("kazim@test.com");

        verify(cartItemRepository, times(1)).deleteByUserEmail("kazim@test.com");
    }
}