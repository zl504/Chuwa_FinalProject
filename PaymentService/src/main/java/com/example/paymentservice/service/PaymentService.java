package com.example.paymentservice.service;

import com.example.paymentservice.domain.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentService {
    Payment charge(UUID orderId, Long userId, BigDecimal amount);
    List<Payment> byOrder(UUID orderId);
    List<Payment> byUser(Long userId);
}
