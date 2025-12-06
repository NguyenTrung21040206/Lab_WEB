package com.example.product_management.repository;

import com.example.product_management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

        // --- BASIC CRUD / SEARCH METHODS ---

        List<Product> findByCategory(String category);

        List<Product> findByNameContaining(String keyword);

        Page<Product> findByNameContaining(String keyword, Pageable pageable);

        List<Product> findByCategory(String category, Sort sort);

        Page<Product> findByCategory(String category, Pageable pageable);

        // --- ADVANCED SEARCH (MULTI CRITERIA) ---
        @Query("SELECT p FROM Product p WHERE " +
                        "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
                        "(:category IS NULL OR p.category = :category) AND " +
                        "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR p.price <= :maxPrice)")
        List<Product> searchProducts(@Param("name") String name,
                        @Param("category") String category,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice);

        // --- CATEGORY LIST ---
        @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
        List<String> findAllCategories();

        // --- DASHBOARD / STATISTICS ---

        // Count products by category
        @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
        long countByCategory(@Param("category") String category);

        // Total inventory value
        @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
        BigDecimal calculateTotalValue();

        // Average product price
        @Query("SELECT AVG(p.price) FROM Product p")
        BigDecimal calculateAveragePrice();

        // Low stock products (quantity < threshold)
        @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
        List<Product> findLowStockProducts(@Param("threshold") int threshold);

        // Recent products (last n added, use Pageable)
        @Query("SELECT p FROM Product p ORDER BY p.id DESC")
        List<Product> findRecentProducts(Pageable pageable);
}
