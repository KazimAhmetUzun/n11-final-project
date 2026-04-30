package com.n11.cartservice.controller;

import com.n11.cartservice.request.AddCartItemRequest;
import com.n11.cartservice.request.UpdateCartItemRequest;
import com.n11.cartservice.response.CartItemResponse;
import com.n11.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartItemResponse addItem(@Valid @RequestBody AddCartItemRequest request) {
        return cartService.addItem(request);
    }

    @GetMapping("/{userEmail}")
    public List<CartItemResponse> getCart(@PathVariable String userEmail) {
        return cartService.getCart(userEmail);
    }

    @PutMapping("/{id}")
    public CartItemResponse updateItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItem(id, request);
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        cartService.removeItem(id);
    }

    @DeleteMapping("/clear/{userEmail}")
    public void clearCart(@PathVariable String userEmail) {
        cartService.clearCart(userEmail);
    }
}