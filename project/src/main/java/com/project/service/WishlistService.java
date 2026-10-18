package com.project.service;

import com.project.dto.WishlistRequest;
import com.project.dto.WishlistResponse;
import com.project.entity.Product;
import com.project.entity.User;
import com.project.entity.Wishlist;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.ProductRepository;
import com.project.repository.UserRepository;
import com.project.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public WishlistResponse addToWishlist(WishlistRequest wishlistRequest) {

        User user = userRepository.findById(wishlistRequest.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(wishlistRequest.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return convertToResponse(savedWishlist);
    }

    public List<WishlistResponse> getAllWishlistItems() {

        return wishlistRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<WishlistResponse> getWishlistItemById(Long id) {

        return wishlistRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<WishlistResponse> getAllWishlistItemsByUserId(Long userId) {

        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<WishlistResponse> getAllWishlistItemsByProductId(Long productId) {

        return wishlistRepository.findByProductId(productId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public boolean removeFromWishlist(Long id) {

        if (wishlistRepository.existsById(id)) {
            wishlistRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private WishlistResponse convertToResponse(Wishlist wishlist) {

        return new WishlistResponse(
                wishlist.getId(),
                wishlist.getUser().getId(),
                wishlist.getProduct().getId()
        );
    }
}