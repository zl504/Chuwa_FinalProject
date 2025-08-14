package com.example.paymentservice.event;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderPlacedEvent {
    public UUID orderId;
    public Long userId;
    public BigDecimal total;
    public Instant createdAt;
    public List<Line> lines;

    public static class Line {
        public String itemId;
        public int quantity;
        public BigDecimal unitPrice;
    }
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
