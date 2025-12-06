package com.example.product_management.service;

import com.example.product_management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    // --- CRUD ---
    List<Product> getAllProducts();

    Page<Product> getAllProducts(Pageable pageable);

    List<Product> getAllProducts(Sort sort);

    Optional<Product> getProductById(Long id);

    Product saveProduct(Product product);

    void deleteProduct(Long id);

    // --- SEARCH ---
    List<Product> searchProducts(String keyword);

    Page<Product> searchProducts(String keyword, Pageable pageable);

    List<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);

    // --- CATEGORY ---
    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByCategory(String category, Sort sort);

    Page<Product> getProductsByCategory(String category, Pageable pageable);

    List<String> getAllCategories();

    // --- DASHBOARD / STATISTICS ---
    long countByCategory(String category);

    BigDecimal calculateTotalValue();

    BigDecimal calculateAveragePrice();

    List<Product> findLowStockProducts(int threshold);

    List<Product> findRecentProducts(int limit);
}
