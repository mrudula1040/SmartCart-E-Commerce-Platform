package com.project.service;

import com.project.entity.OrderItems;
import com.project.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemsService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemsService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItems saveOrderItem(OrderItems orderItems) {
        return orderItemRepository.save(orderItems);
    }

    public List<OrderItems> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItems> getOrderItemsById(Long id) {
        return orderItemRepository.findById(id);
    }

    public List<OrderItems> getOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public List<OrderItems> getOrderItemsByProductId(Long productId) {
        return orderItemRepository.findByProductId(productId);
    }

    public Optional<OrderItems> updateOrderItem(
            Long id,
            OrderItems orderItemsDetails) {

        return orderItemRepository.findById(id)
                .map(existingItem -> {

                    existingItem.setQuantity(
                            orderItemsDetails.getQuantity()
                    );

                    existingItem.setPrice(
                            orderItemsDetails.getPrice()
                    );

                    existingItem.setOrder(
                            orderItemsDetails.getOrder()
                    );

                    existingItem.setProduct(
                            orderItemsDetails.getProduct()
                    );

                    return orderItemRepository.save(existingItem);
                });
    }

    public boolean deleteOrderItems(Long id) {

        if (orderItemRepository.existsById(id)) {
            orderItemRepository.deleteById(id);
            return true;
        }

        return false;
    }
}