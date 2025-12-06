package com.example.product_management.controller;

import com.example.product_management.entity.Product;
import com.example.product_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --- LIST PRODUCTS (with pagination, sort, filter) ---
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            Model model) {

        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;

        if (keyword != null && !keyword.isEmpty()) {
            // Search by keyword with pagination
            productPage = productService.searchProducts(keyword, pageable);
        } else if (category != null && !category.isEmpty()) {
            // Filter by category with pagination
            productPage = productService.getProductsByCategory(category, pageable);
        } else {
            // All products with pagination
            productPage = productService.getAllProducts(pageable);
        }

        List<String> categories = productService.getAllCategories();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("category", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categories", categories);

        return "product-list";
    }

    // --- SHOW FORM FOR NEW PRODUCT ---
    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    // --- SHOW FORM FOR EDIT PRODUCT ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }

    // --- SAVE PRODUCT (CREATE/UPDATE) ---
    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "product-form";
        }

        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", "Product saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/products";
    }

    // --- DELETE PRODUCT ---
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // --- ADVANCED SEARCH (non-paginated) ---
    @GetMapping("/advanced-search")
    public String advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {

        List<Product> products = productService.searchProducts(name, category, minPrice, maxPrice);
        List<String> categories = productService.getAllCategories();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("name", name);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "product-list";
    }
}
