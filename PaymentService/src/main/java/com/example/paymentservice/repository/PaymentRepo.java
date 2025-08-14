package com.example.paymentservice.repository;

import com.example.paymentservice.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepo extends JpaRepository<Payment, UUID> {
    List<Payment> findByOrderId(UUID orderId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);
}
