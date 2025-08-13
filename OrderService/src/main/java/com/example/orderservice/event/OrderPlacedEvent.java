package com.example.orderservice.event;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderPlacedEvent {
    private UUID orderId;
    private Long userId;
    private BigDecimal total;
    private Instant createdAt;
    private List<Line> lines;

    public OrderPlacedEvent() {
    }

    public OrderPlacedEvent(UUID orderId, Long userId, BigDecimal total, Instant createdAt, List<Line> lines) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.createdAt = createdAt;
        this.lines = lines;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public static class Line {

        private String itemId;
        private int quantity;
        private BigDecimal unitPrice;

        public Line() {
        }

        public Line(String itemId, int quantity, BigDecimal unitPrice) {
            this.itemId = itemId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }
    }
}
