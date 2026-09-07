package com.project.service;

import com.project.entity.Product;
import com.project.repository.ProductRepository;
import org.springframework.data.domain.Page ;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> searchProduct(String productName){
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }
    public List<Product> filterByCategory(Long categoryId){
        return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> filterByPrice(Double minPrice, Double maxPrice){
        return productRepository.findByPriceBetween(minPrice,maxPrice);
    }
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }
    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public Page<Product> getProductsWithPagination(Pageable pageable){
        return productRepository.findAll(pageable);
    }

}
