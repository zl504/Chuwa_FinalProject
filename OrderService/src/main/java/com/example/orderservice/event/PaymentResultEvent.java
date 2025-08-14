package com.example.orderservice.event;

import java.util.UUID;

public class PaymentResultEvent {
    public UUID paymentId;
    private UUID orderId;
    private Long userId;
    private PaymentStatus status;
//    private String reason; // null if success

    public enum PaymentStatus {
        SUCCESS,
        FAILED
    }

    public PaymentResultEvent() {
    }

    public PaymentResultEvent(UUID paymentId,UUID orderId, Long userId, PaymentStatus status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
//        this.reason = reason;
    }

    // Getters and setters
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
}
