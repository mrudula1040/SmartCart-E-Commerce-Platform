package com.project.controller;

import com.project.dto.CartRequest;
import com.project.dto.CartResponse;
import com.project.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @Valid @RequestBody CartRequest cartRequest) {

        return ResponseEntity.ok(
                cartService.addToCart(cartRequest)
        );
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCartItems() {

        return ResponseEntity.ok(
                cartService.getAllCartItems()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCartItemById(
            @PathVariable Long id) {

        return cartService.getCartItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartResponse>> getCartItemsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCartItemsByUserId(userId)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<CartResponse>> getCartItemsByProductId(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                cartService.getCartItemsByProductId(productId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartResponse> updateCartItem(
            @PathVariable Long id,
            @Valid @RequestBody CartRequest cartRequest) {

        return cartService.updateCartItem(id, cartRequest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(
            @PathVariable Long id) {

        if (cartService.removeFromCart(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}