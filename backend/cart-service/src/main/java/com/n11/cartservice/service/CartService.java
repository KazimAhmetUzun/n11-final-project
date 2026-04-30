package com.n11.cartservice.service;


import com.n11.cartservice.request.AddCartItemRequest;
import com.n11.cartservice.request.UpdateCartItemRequest;
import com.n11.cartservice.response.CartItemResponse;

import java.util.List;

public interface CartService {

    CartItemResponse addItem(AddCartItemRequest request);

    List<CartItemResponse> getCart(String userEmail);

    CartItemResponse updateItem(Long id, UpdateCartItemRequest request);

    void removeItem(Long id);

    void clearCart(String userEmail);
}