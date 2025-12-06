package com.example.product_management.service;

import com.example.product_management.entity.Product;
import com.example.product_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- CRUD ---
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getAllProducts(Sort sort) {
        return productRepository.findAll(sort);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // --- SEARCH ---
    @Override
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContaining(keyword);
    }

    @Override
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }

    @Override
    public List<Product> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }

    // --- CATEGORY ---
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getProductsByCategory(String category, Sort sort) {
        return productRepository.findByCategory(category, sort);
    }

    @Override
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }

    @Override
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    // --- DASHBOARD / STATISTICS ---
    @Override
    public long countByCategory(String category) {
        return productRepository.countByCategory(category);
    }

    @Override
    public BigDecimal calculateTotalValue() {
        return productRepository.calculateTotalValue();
    }

    @Override
    public BigDecimal calculateAveragePrice() {
        return productRepository.calculateAveragePrice();
    }

    @Override
    public List<Product> findLowStockProducts(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    @Override
    public List<Product> findRecentProducts(int limit) {
        return productRepository.findRecentProducts(PageRequest.of(0, limit));
    }
}
