package com.project.service;

import com.project.repository.OrderRepository;
import com.project.repository.ProductRepository;
import com.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AdminService(UserRepository userRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;

        this.productRepository = productRepository;
    }

    private final ProductRepository productRepository;

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> dashboard = new HashMap<>();
        long totalCustomers = userRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = orderRepository.count();
        double totalSales = orderRepository.findAll().stream().filter(order -> order.getTotalAmount()!=null).mapToDouble(order->order.getTotalAmount()).sum();
        dashboard.put("totalCustomers",totalCustomers);
        dashboard.put("totalProducts",totalProducts);
        dashboard.put("totalOrders",totalOrders);
        dashboard.put("totalSales",totalSales);

        return dashboard;
    }
}
