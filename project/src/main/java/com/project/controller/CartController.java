package com.project.controller;

import com.project.entity.Cart;
import com.project.service.CartService;
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
    public ResponseEntity<Cart> addToCart(@RequestBody Cart cart){
        return ResponseEntity.ok(cartService.addToCart(cart));
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllCartItems(){
        return ResponseEntity.ok(cartService.getAllCartItems());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartItemById(@PathVariable Long id){
        return cartService.getCartItemById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long id,@RequestBody Cart cart){
        return ResponseEntity.ok(cartService.updateCartItem(id, cart));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id){
        cartService.removeFromCart(id);
        return ResponseEntity.noContent().build();
    }
}
