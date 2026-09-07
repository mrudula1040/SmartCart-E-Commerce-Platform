package com.project.repository;

import com.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByProductNameContainingIgnoreCase(String productName);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByPriceBetween(Double minPrice , Double maxPrice);
}
