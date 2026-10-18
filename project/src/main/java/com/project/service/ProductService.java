package com.project.service;

import com.project.dto.ProductRequest;
import com.project.dto.ProductResponse;
import com.project.entity.Category;
import com.project.entity.Product;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.CategoryRepository;
import com.project.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Convert Product entity to ProductResponse DTO
    private ProductResponse convertToResponse(Product product) {

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setImgURL(product.getImgURL());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getCategoryName());
        }

        return response;
    }

    // Add Product
    public ProductResponse saveProduct(ProductRequest productRequest) {

        Product product = new Product();

        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setImgURL(productRequest.getImgURL());

        if (productRequest.getCategoryId() != null) {

            Category category = categoryRepository
                    .findById(productRequest.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category not found with ID: "
                                            + productRequest.getCategoryId()));

            product.setCategory(category);
        }

        Product savedProduct = productRepository.save(product);

        return convertToResponse(savedProduct);
    }

    // Get All Products
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Get Product By ID
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + id));

        return convertToResponse(product);
    }

    // Update Product
    public ProductResponse updateProduct(
            Long id,
            ProductRequest productRequest) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + id));

        existingProduct.setProductName(productRequest.getProductName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStock(productRequest.getStock());
        existingProduct.setImgURL(productRequest.getImgURL());

        if (productRequest.getCategoryId() != null) {

            Category category = categoryRepository
                    .findById(productRequest.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category not found with ID: "
                                            + productRequest.getCategoryId()));
            existingProduct.setCategory(category);
        }

        Product updatedProduct =
                productRepository.save(existingProduct);

        return convertToResponse(updatedProduct);
    }

    // Delete Product
    public String deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found with ID: " + id));

        productRepository.delete(product);

        return "Product deleted successfully";
    }

    // Search Product
    public List<ProductResponse> searchProducts(String keyword) {

        return productRepository
                .findByProductNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Filter By Category
    public List<ProductResponse> getProductsByCategory(Long categoryId) {

        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Filter By Price
    public List<ProductResponse> getProductsByPrice(
            Double minPrice,
            Double maxPrice) {

        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Pagination
    public Page<ProductResponse> getProductsWithPagination(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAll(pageable)
                .map(this::convertToResponse);
    }
}