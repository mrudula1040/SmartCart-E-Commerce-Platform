package com.project.service;

import com.project.dto.PaymentRequest;
import com.project.dto.PaymentResponse;
import com.project.entity.Order;
import com.project.entity.Payment;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.OrderRepository;
import com.project.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));

        Payment payment = new Payment();

        payment.setAmount(order.getTotalAmount());
        payment.setOrder(order);
        payment.setPaymentMethod(paymentRequest.getPaymentMethod());

        if (paymentRequest.getPaymentStatus() == null
                || paymentRequest.getPaymentStatus().isBlank()) {
            payment.setPaymentStatus("PENDING");
        } else {
            payment.setPaymentStatus(paymentRequest.getPaymentStatus());
        }

        Payment savedPayment = paymentRepository.save(payment);

        return convertToResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public Optional<PaymentResponse> getPaymentById(Long id) {

        return paymentRepository.findById(id)
                .map(this::convertToResponse);
    }

    public Optional<PaymentResponse> getPaymentByOrderId(Long orderId) {

        return paymentRepository.findByOrderId(orderId)
                .map(this::convertToResponse);
    }

    public Optional<PaymentResponse> updatePaymentStatus(
            Long id,
            String status) {

        return paymentRepository.findById(id)
                .map(existingPayment -> {

                    existingPayment.setPaymentStatus(status);

                    Payment updatedPayment =
                            paymentRepository.save(existingPayment);

                    return convertToResponse(updatedPayment);
                });
    }

    public boolean deletePayment(Long id) {

        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private PaymentResponse convertToResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getOrder().getId(),
                payment.getPaymentStatus(),
                payment.getPaymentMethod(),
                payment.getPaymentDate()
        );
    }
}