package com.example.paymentservice.event;

import java.util.UUID;

public class PaymentResultEvent {
    public UUID paymentId;
    public UUID orderId;
    public Long userId;
    public String status; // "SUCCESS" or "FAILED"
//    public Instant occurredAt;

    public enum PaymentStatus {
        SUCCESS,
        FAILED
    }

    public PaymentResultEvent() { }
    public PaymentResultEvent(UUID paymentId,
                              UUID orderId, Long userId,
                              String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.userId = userId;
//        this.amount = amount;
        this.status = status;
//        this.occurredAt = occurredAt;
    }
// Getters and setters
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
