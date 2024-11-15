package com.example.ProductCategoryAPI.service;

import com.example.ProductCategoryAPI.model.Category;
import com.example.ProductCategoryAPI.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get category by ID
    public Optional<Category> getCategoryById(int id) {
        return categoryRepository.findById(id);
    }

    // Create or update category
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Delete category by ID
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }
}
