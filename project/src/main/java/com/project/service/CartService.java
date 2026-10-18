package com.project.service;

import com.project.dto.CartRequest;
import com.project.dto.CartResponse;
import com.project.entity.Cart;
import com.project.entity.Product;
import com.project.entity.User;
import com.project.repository.CartRepository;
import com.project.repository.ProductRepository;
import com.project.repository.UserRepository;
import com.project.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartResponse addToCart(CartRequest cartRequest) {

        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(cartRequest.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        return convertToResponse(savedCart);
    }

    public List<CartResponse> getAllCartItems() {

        return cartRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<CartResponse> getCartItemById(Long id) {

        return cartRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<CartResponse> getCartItemsByUserId(Long userId) {

        return cartRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<CartResponse> getCartItemsByProductId(Long productId) {

        return cartRepository.findByProductId(productId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<CartResponse> updateCartItem(Long id,
                                                 CartRequest cartRequest) {

        return cartRepository.findById(id)
                .map(existingCart -> {

                    existingCart.setQuantity(cartRequest.getQuantity());

                    Cart updatedCart = cartRepository.save(existingCart);

                    return convertToResponse(updatedCart);
                });
    }

    public boolean removeFromCart(Long id) {

        if (cartRepository.existsById(id)) {
            cartRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private CartResponse convertToResponse(Cart cart) {

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                cart.getProduct().getId(),
                cart.getQuantity()
        );
    }
}