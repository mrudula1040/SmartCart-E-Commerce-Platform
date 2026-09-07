package com.project.service;

import com.project.entity.OrderItems;
import com.project.repository.OrderItemRepository;
import com.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemsService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemsService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItems saveOrderItem(OrderItems orderItems){
        return orderItemRepository.save(orderItems);
    }
    public List<OrderItems> getAllOrderItems(){
        return orderItemRepository.findAll();
    }
    public Optional<OrderItems> getOrderItemsById(Long id){
        return orderItemRepository.findById(id);
    }
    public void deleteOrderItems(Long id){
        orderItemRepository.deleteById(id);
    }
}
