package com.example.ProductCategoryAPI.controller;

import com.example.ProductCategoryAPI.model.Product;
import com.example.ProductCategoryAPI.model.Category;
import com.example.ProductCategoryAPI.repository.ProductRepository;
import com.example.ProductCategoryAPI.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Validate category existence
        if (product.getCategory() == null || product.getCategory().getId() == 0) {
            return ResponseEntity.badRequest().body(null);
        }
        Category category = categoryRepository.findById(product.getCategory().getId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body(null);
        }
        // Set the category and save product
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestBody Product productDetails) {
        return (ResponseEntity<Product>) productRepository.findById(id).map(existingProduct -> {
            // Validate category existence
            if (productDetails.getCategory() != null && productDetails.getCategory().getId() != 0) {
                Category category = categoryRepository.findById(productDetails.getCategory().getId()).orElse(null);
                if (category == null) {
                    // Return Bad Request if category doesn't exist
                    return ResponseEntity.badRequest().body(null);  // Return ResponseEntity<Product> here
                }
                existingProduct.setCategory(category);
            }

            // Update other fields
            existingProduct.setName(productDetails.getName());
            existingProduct.setPrice(productDetails.getPrice());

            // Save and return updated product
            Product updatedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(updatedProduct);
        }).orElse(ResponseEntity.notFound().build());
    }


    // Delete a product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
