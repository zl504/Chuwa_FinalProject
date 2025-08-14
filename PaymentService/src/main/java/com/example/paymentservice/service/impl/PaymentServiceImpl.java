package com.example.paymentservice.service.impl;

import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.domain.PaymentStatus;
import com.example.paymentservice.event.PaymentCompletedEvent;
import com.example.paymentservice.event.PaymentEventProducer;
import com.example.paymentservice.repository.PaymentRepo;
import com.example.paymentservice.service.PaymentService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo repo;
//    private final KafkaTemplate<String, Object> kafka;
    private final Random random = new Random();
    private final PaymentEventProducer producer;

    public PaymentServiceImpl(PaymentRepo repo, PaymentEventProducer producer) {
        this.repo = repo;
        this.producer = producer;
    }

    @Override
    @Transactional
    public Payment charge(UUID orderId, Long userId, BigDecimal amount) {
        var p = new Payment();
        p.setOrderId(orderId);
        p.setUserId(userId);
        p.setAmount(amount);
        p.setStatus(PaymentStatus.PENDING);
        p = repo.save(p);

        // Simulate gateway call (80% success)
        boolean ok = random.nextDouble() < 0.8;

        p.setStatus(ok ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        p = repo.save(p);

        // Emit event
        var evt = new PaymentCompletedEvent(
                p.getId(),
                p.getOrderId(), p.getUserId(),
                p.getStatus().name()
        );
        producer.publishPaymentResult(evt);
        return p;
    }

    @Override public List<Payment> byOrder(UUID orderId) {
        return repo.findByOrderId(orderId);
    }

    @Override public List<Payment> byUser(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }

}
