package com.project.dto;

import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long userId;
    private Double totalAmount;
    private String status;
    private LocalDateTime orderDate;

    public OrderResponse() {
    }

    public OrderResponse(Long id,
                         Long userId,
                         Double totalAmount,
                         String status,
                         LocalDateTime orderDate) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}