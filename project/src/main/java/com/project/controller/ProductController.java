package com.project.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.project.entity.Product;
import com.project.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product){
        return ResponseEntity.ok(productService.saveProduct(product));
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String productName){
        return ResponseEntity.ok(productService.searchProduct(productName));
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> filterByCategory(@PathVariable Long categoryId){
        return  ResponseEntity.ok(productService.filterByCategory(categoryId));
    }
    @GetMapping("/price")
    public ResponseEntity<List<Product>> filterByPrice(@RequestParam Double minPrice,
                                                       Double maxPrice){
        return ResponseEntity.ok(productService.filterByPrice(minPrice,maxPrice));
    }
    @GetMapping("/page")
    public ResponseEntity<Page<Product>> getProductsWithPagination(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size){
        PageRequest pageRequest=PageRequest.of(page,size);
        return ResponseEntity.ok(productService.getProductsWithPagination(pageRequest));
    }

}