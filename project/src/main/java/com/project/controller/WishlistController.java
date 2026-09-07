package com.project.controller;

import com.project.entity.Wishlist;
import com.project.service.WishlistService;
import jakarta.persistence.GeneratedValue;
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
    public ResponseEntity<Wishlist> addToWishlist(@RequestBody Wishlist wishlist){
        return ResponseEntity.ok(wishlistService.addToWishlist(wishlist));
    }
    @GetMapping
    public ResponseEntity<List<Wishlist>> getAllWishlistItems(){
        return ResponseEntity.ok(wishlistService.getAllWishlistitems());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistItemById(@PathVariable Long id){
        return wishlistService.getWishlistItemById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeFromWishlist(@PathVariable Long id){
        wishlistService.removeFromWishlist(id);
        return ResponseEntity.noContent().build();
    }
}
