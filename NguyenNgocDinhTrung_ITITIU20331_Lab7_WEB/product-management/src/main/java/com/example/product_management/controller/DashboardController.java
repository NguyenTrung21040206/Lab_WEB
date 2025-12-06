package com.example.product_management.controller;

import com.example.product_management.entity.Product;
import com.example.product_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProductService productService;

    @Autowired
    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showDashboard(Model model) {

        // --- General statistics ---
        int totalProducts = productService.getAllProducts().size();
        model.addAttribute("totalProducts", totalProducts);

        model.addAttribute("totalValue", productService.calculateTotalValue());
        model.addAttribute("averagePrice", productService.calculateAveragePrice());

        // --- Low stock products (qty < 10) ---
        List<Product> lowStock = productService.findLowStockProducts(10);
        model.addAttribute("lowStock", lowStock);

        // --- Recent products (last 5 added) ---
        List<Product> recentProducts = productService.findRecentProducts(5);
        model.addAttribute("recentProducts", recentProducts);

        // --- Category data for Pie Chart ---
        List<String> categories = productService.getAllCategories();
        List<Long> categoryCounts = categories.stream()
                .map(productService::countByCategory)
                .collect(Collectors.toList());

        model.addAttribute("categoryLabels", categories);
        model.addAttribute("categoryCounts", categoryCounts);

        return "dashboard";
    }
}
