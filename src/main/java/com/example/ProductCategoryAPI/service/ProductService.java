package com.example.ProductCategoryAPI.service;

import com.example.ProductCategoryAPI.model.Product;
import com.example.ProductCategoryAPI.model.Category;
import com.example.ProductCategoryAPI.repository.ProductRepository;
import com.example.ProductCategoryAPI.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    // Create product
    public Optional<Product> createProduct(Product product) {
        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isEmpty()) {
            return Optional.empty();
        }
        product.setCategory(category.get());
        return Optional.of(productRepository.save(product));
    }

    // Update product
    public Optional<Product> updateProduct(int id, Product productDetails) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            return Optional.empty();
        }
        Product product = existingProduct.get();
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());

        // Update category if it exists
        Optional<Category> category = categoryRepository.findById(productDetails.getCategory().getId());
        if (category.isPresent()) {
            product.setCategory(category.get());
        }

        return Optional.of(productRepository.save(product));
    }

    // Delete product by ID
    public boolean deleteProduct(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
