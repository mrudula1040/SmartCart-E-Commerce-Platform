package com.project.service;

import com.project.entity.Payment;
import com.project.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);

    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment updatePaymentStatus(Long id, String status){
        Payment exsistingPayment = paymentRepository.findById(id).orElseThrow(()->new RuntimeException("Payment not found"));
        exsistingPayment.setPaymentStatus(status);
        return paymentRepository.save(exsistingPayment);
    }
    public void deletePayment(Long id){
        paymentRepository.deleteById(id);
    }
}