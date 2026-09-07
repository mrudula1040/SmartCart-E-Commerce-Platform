package com.project.service;

import com.project.entity.Order;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public Order createOrder(Order order){
        return orderRepository.save(order);
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Optional<Order> getOrderById(Long id){
        return orderRepository.findById(id);
    }
    public Order updateOrderStatus(Long id, String status){
        Order existingOrder = orderRepository.findById(id).orElseThrow(()->new RuntimeException("Order not found"));
        existingOrder.setStatus(status);
        return orderRepository.save(existingOrder);
    }
    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }
}
