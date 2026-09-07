package com.project.service;

import com.project.entity.Cart;
import com.project.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;


    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addToCart(Cart cart){
        return cartRepository.save(cart);

    }
    public List<Cart> getAllCartItems(){
        return cartRepository.findAll();
    }
    public Optional<Cart> getCartItemById(Long id){
        return cartRepository.findById(id);
    }
    public Cart updateCartItem(Long id,Cart cart){
        Cart existingCart = cartRepository.findById(id).orElseThrow(()->new RuntimeException("Cart item not found"));
        existingCart.setQuantity(cart.getQuantity());
        return cartRepository.save(existingCart);
    }
    public void removeFromCart(Long id){
        cartRepository.deleteById(id);
    }
}
