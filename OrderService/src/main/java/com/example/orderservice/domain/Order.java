package com.example.orderservice.domain;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Table("orders")
public class Order {
    @PrimaryKey
    private UUID id;

    @Column("user_id")
    private Long userId;

    private BigDecimal total = BigDecimal.ZERO;

    private String status = OrderStatus.PENDING.name();

    @Column("created_at")
    private Instant createdAt = Instant.now();

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.UDT, userTypeName = "order_line")
    private List<OrderLineUdt> lines = new ArrayList<>();

    public void addLine(OrderLineUdt l) {
        lines.add(l);
        total = total.add(l.getLineTotal());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<OrderLineUdt> getLines() {
        return lines;
    }

    public void setLines(List<OrderLineUdt> lines) {
        this.lines = lines;
    }
}
