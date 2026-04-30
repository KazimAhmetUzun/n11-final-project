package com.n11.cartservice.service.impl;

import com.n11.cartservice.entity.CartItem;
import com.n11.cartservice.exception.CartItemNotFoundException;
import com.n11.cartservice.repository.CartItemRepository;
import com.n11.cartservice.request.AddCartItemRequest;
import com.n11.cartservice.request.UpdateCartItemRequest;
import com.n11.cartservice.response.CartItemResponse;
import com.n11.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;

    @Override
    public CartItemResponse addItem(AddCartItemRequest request) {
        log.info("Adding item to cart. userEmail={}, productId={}, quantity={}",
                request.getUserEmail(), request.getProductId(), request.getQuantity());

        CartItem item = cartItemRepository
                .findByUserEmailAndProductId(request.getUserEmail(), request.getProductId())
                .map(existingItem -> {
                    log.info("Cart item already exists. Increasing quantity. cartItemId={}, oldQuantity={}, addedQuantity={}",
                            existingItem.getId(), existingItem.getQuantity(), request.getQuantity());

                    existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
                    return existingItem;
                })
                .orElseGet(() -> {
                    log.info("Creating new cart item. userEmail={}, productId={}",
                            request.getUserEmail(), request.getProductId());

                    return CartItem.builder()
                            .userEmail(request.getUserEmail())
                            .productId(request.getProductId())
                            .productName(request.getProductName())
                            .price(request.getPrice())
                            .quantity(request.getQuantity())
                            .build();
                });

        CartItem savedItem = cartItemRepository.save(item);

        log.info("Cart item saved successfully. cartItemId={}, userEmail={}, productId={}, quantity={}",
                savedItem.getId(), savedItem.getUserEmail(), savedItem.getProductId(), savedItem.getQuantity());

        return toResponse(savedItem);
    }

    @Override
    public List<CartItemResponse> getCart(String userEmail) {
        log.info("Fetching cart. userEmail={}", userEmail);

        List<CartItemResponse> cartItems = cartItemRepository.findByUserEmail(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();

        log.info("Cart fetched successfully. userEmail={}, itemCount={}", userEmail, cartItems.size());

        return cartItems;
    }

    @Override
    public CartItemResponse updateItem(Long id, UpdateCartItemRequest request) {
        log.info("Updating cart item. cartItemId={}, newQuantity={}", id, request.getQuantity());

        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Update failed. Cart item not found. cartItemId={}", id);
                    return new CartItemNotFoundException(id);
                });

        item.setQuantity(request.getQuantity());

        CartItem updatedItem = cartItemRepository.save(item);

        log.info("Cart item updated successfully. cartItemId={}, quantity={}",
                updatedItem.getId(), updatedItem.getQuantity());

        return toResponse(updatedItem);
    }

    @Override
    public void removeItem(Long id) {
        log.info("Removing cart item. cartItemId={}", id);

        if (!cartItemRepository.existsById(id)) {
            log.warn("Remove failed. Cart item not found. cartItemId={}", id);
            throw new CartItemNotFoundException(id);
        }

        cartItemRepository.deleteById(id);

        log.info("Cart item removed successfully. cartItemId={}", id);
    }

    @Override
    @Transactional
    public void clearCart(String userEmail) {
        log.info("Clearing cart. userEmail={}", userEmail);

        cartItemRepository.deleteByUserEmail(userEmail);

        log.info("Cart cleared successfully. userEmail={}", userEmail);
    }

    private CartItemResponse toResponse(CartItem item) {
        BigDecimal totalPrice = item.getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponse.builder()
                .id(item.getId())
                .userEmail(item.getUserEmail())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }
}