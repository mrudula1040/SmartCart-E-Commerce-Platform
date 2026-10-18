package com.project.controller;

import com.project.dto.WishlistRequest;
import com.project.dto.WishlistResponse;
import com.project.service.WishlistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<WishlistResponse> addToWishlist(
            @Valid @RequestBody WishlistRequest wishlistRequest) {

        return ResponseEntity.ok(
                wishlistService.addToWishlist(wishlistRequest)
        );
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getAllWishlistItems() {

        return ResponseEntity.ok(
                wishlistService.getAllWishlistItems()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<WishlistResponse> getWishlistItemById(
            @PathVariable Long id) {

        return wishlistService.getWishlistItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WishlistResponse>> getWishlistItemByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                wishlistService.getAllWishlistItemsByUserId(userId)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<WishlistResponse>> getWishlistItemByProductId(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                wishlistService.getAllWishlistItemsByProductId(productId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long id) {

        if (wishlistService.removeFromWishlist(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}