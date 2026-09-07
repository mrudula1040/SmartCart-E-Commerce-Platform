package com.project.controller;

import com.project.entity.OrderItems;
import com.project.service.OrderItemsService;
import org.apache.coyote.Response;
import org.hibernate.dialect.unique.CreateTableUniqueDelegate;
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
    public ResponseEntity<OrderItems> createOrderItems(@RequestBody OrderItems orderItems){
        return ResponseEntity.ok(orderItemsService.saveOrderItem(orderItems));
    }
    @GetMapping
    public ResponseEntity<List<OrderItems>> getAllOrderItems(){
        return ResponseEntity.ok(orderItemsService.getAllOrderItems());
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderItems> getOrderItemsById(@PathVariable Long id){
        return orderItemsService.getOrderItemsById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id){
        orderItemsService.deleteOrderItems(id);
        return ResponseEntity.noContent().build();
    }
}
