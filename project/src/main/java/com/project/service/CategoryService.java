package com.project.service;

import com.project.dto.CategoryRequest;
import com.project.dto.CategoryResponse;
import com.project.entity.Category;
import com.project.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Convert Category entity to CategoryResponse DTO
    private CategoryResponse convertToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());

        return response;
    }

    // Save Category
    public CategoryResponse saveCategory(CategoryRequest categoryRequest) {

        Category category = new Category();

        category.setCategoryName(categoryRequest.getCategoryName());
        category.setDescription(categoryRequest.getDescription());

        Category savedCategory = categoryRepository.save(category);

        return convertToResponse(savedCategory);
    }

    // Get All Categories
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // Update Category
    public Optional<CategoryResponse> updateCategory(
            Long id,
            CategoryRequest categoryRequest) {

        return categoryRepository.findById(id)
                .map(category -> {

                    category.setCategoryName(
                            categoryRequest.getCategoryName()
                    );

                    category.setDescription(
                            categoryRequest.getDescription()
                    );

                    Category updatedCategory =
                            categoryRepository.save(category);

                    return convertToResponse(updatedCategory);
                });
    }

    // Get Category By ID
    public Optional<CategoryResponse> getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .map(this::convertToResponse);
    }

    // Delete Category
    public boolean deleteCategory(Long id) {

        if (categoryRepository.existsById(id)) {

            categoryRepository.deleteById(id);
            return true;
        }

        return false;
    }
}