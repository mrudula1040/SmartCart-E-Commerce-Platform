package com.project.controller;

import com.project.entity.OrderItems;
import com.project.service.OrderItemsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemsController {

    private final OrderItemsService orderItemsService;

    public OrderItemsController(OrderItemsService orderItemsService) {
        this.orderItemsService = orderItemsService;
    }

    @PostMapping
    public ResponseEntity<OrderItems> createOrderItem(
           @Valid @RequestBody OrderItems orderItems) {

        return ResponseEntity.ok(
                orderItemsService.saveOrderItem(orderItems)
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderItems>> getAllOrderItems() {

        return ResponseEntity.ok(
                orderItemsService.getAllOrderItems()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItems> getOrderItemsById(
            @PathVariable Long id) {

        return orderItemsService.getOrderItemsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItems>> getOrderItemsByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderItemsService.getOrderItemsByOrderId(orderId)
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItems>> getOrderItemsByProductId(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                orderItemsService.getOrderItemsByProductId(productId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItems> updateOrderItem(
            @PathVariable Long id,
           @Valid @RequestBody OrderItems orderItemsDetails) {

        return orderItemsService
                .updateOrderItem(id, orderItemsDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long id) {

        if (orderItemsService.deleteOrderItems(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}