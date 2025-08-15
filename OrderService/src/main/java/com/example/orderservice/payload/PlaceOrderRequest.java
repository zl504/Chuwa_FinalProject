package com.example.orderservice.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderRequest {
//    @NotNull
//    private Long userId;

    @NotNull
    @Size(min = 1, message = "At least one line item is required")
    @Valid
    private List<Line> items;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(List<Line> items) {
        this.items = items;
    }

    public List<Line> getItems() {
        return items;
    }

    public void setItems(List<Line> items) {
        this.items = items;
    }

    // Nested concrete class for a line item
    public static class Line {

        @NotBlank
        private String itemId;

        @Min(1)
        private int quantity;

        public Line() {
        }

        public Line(String itemId, int quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
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

    }
}
