package com.project.service;

import com.project.entity.Wishlist;
import com.project.repository.WishlistRepository;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }
    public Wishlist addToWishlist(Wishlist wishList){
        return wishlistRepository.save(wishList);
    }
    public List<Wishlist> getAllWishlistitems(){
        return wishlistRepository.findAll();
    }
    public Optional<Wishlist> getWishlistItemById(Long id){
        return wishlistRepository.findById(id);
    }
    public void removeFromWishlist(Long id){
        wishlistRepository.deleteById(id) ;
    }
}
