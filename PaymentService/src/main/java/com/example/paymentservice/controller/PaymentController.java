package com.example.paymentservice.controller;

import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.service.PaymentService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private record ChargeRequest(@NotNull UUID orderId,
                                 @NotNull Long userId,
                                 @NotNull @Min(0) BigDecimal amount) {}

    private final PaymentService payments;
    public PaymentController(PaymentService payments) { this.payments = payments; }

    // Optional manual trigger
    @PostMapping("/charge")
    public ResponseEntity<Payment> charge(@RequestBody ChargeRequest req) {


        var p = payments.charge(req.orderId, req.userId, req.amount);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/order/{orderId}")
    public List<Payment> byOrder(@PathVariable UUID orderId) {
        return payments.byOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<Payment> byUser(@PathVariable Long userId) {
        return payments.byUser(userId);
    }
}
