package com.example.orderservice.domain;

import java.math.BigDecimal;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.*;

@UserDefinedType("order_line")
public class OrderLineUdt {

    @Column("item_id")
    private String itemId;

    @Column("item_name")
    private String itemName;

    @Column("unit_price")
    @CassandraType(type = Name.DECIMAL)
    private BigDecimal unitPrice;

    @Column("quantity")
    private int quantity;

    public OrderLineUdt() {}

    public static OrderLineUdt of(String itemId, String itemName, BigDecimal unitPrice, int qty) {
        OrderLineUdt udt = new OrderLineUdt();
        udt.itemId = itemId;
        udt.itemName = itemName;
        udt.unitPrice = unitPrice;
        udt.quantity = qty;
        return udt;
    }

    // getters/setters …
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getLineTotal() {
        return unitPrice == null ? BigDecimal.ZERO
                : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
