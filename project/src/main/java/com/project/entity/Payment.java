package com.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount cannot be negative")
    private Double amount;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    @NotNull(message = "order is required")
    private Order order;
@NotNull(message = "Payment status is required")
    private String paymentStatus;
    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    private LocalDateTime paymentDate;

    public Payment() {
    }

    @PrePersist
    public void setPaymentDateBeforeSave() {

        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }

        if (paymentStatus == null || paymentStatus.isBlank()) {
            paymentStatus = "PENDING";
        }
    }

    public Long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public Order getOrder() {
        return order;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}