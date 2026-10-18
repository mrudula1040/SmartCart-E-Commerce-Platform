
package com.project.service;

import com.project.dto.OrderRequest;
import com.project.dto.OrderResponse;
import com.project.entity.*;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
private final InventoryRepository inventoryRepository;
    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository,
            CartRepository cartRepository,
            OrderItemRepository orderItemRepository, InventoryRepository inventoryRepository) {

        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) {

        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Cart> cartItems =
                cartRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot place an order with an empty cart");
        }

        double calculatedTotal = 0.0;

        for (Cart cartItem : cartItems) {
            if (cartItem.getProduct() == null
                    || cartItem.getQuantity() == null
                    || cartItem.getQuantity() < 1
                    || cartItem.getProduct().getPrice() == null) {
                throw new IllegalArgumentException(
                        "Invalid product or quantity in cart");
            }

            calculatedTotal +=
                    cartItem.getProduct().getPrice()
                            * cartItem.getQuantity();
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(calculatedTotal);

        if (orderRequest.getStatus() == null
                || orderRequest.getStatus().isBlank()) {
            order.setStatus("PENDING");
        } else {
            order.setStatus(orderRequest.getStatus());
        }

        Order savedOrder = orderRepository.save(order);

        for (Cart cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (product.getStock() == null ||
                    product.getStock() < cartItem.getQuantity()) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product: "
                                + product.getProductName());
            }

            // Reduce product stock
            product.setStock(
                    product.getStock() - cartItem.getQuantity()
            );

            // Keep inventory quantity synchronized
            inventoryRepository.findByProductId(product.getId())
                    .ifPresent(inventory -> {
                        inventory.setQuantity(
                                inventory.getQuantity()
                                        - cartItem.getQuantity()
                        );

                        inventoryRepository.save(inventory);
                    });

            OrderItems item = new OrderItems();

            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(product.getPrice());

            orderItemRepository.save(item);
        }
        return convertToResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponse> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToResponse);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<OrderResponse> updateOrderStatus(
            Long id, String status) {

        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setStatus(status);
                    Order updatedOrder =
                            orderRepository.save(existingOrder);
                    return convertToResponse(updatedOrder);
                });
    }

    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderDate()
        );
    }
}