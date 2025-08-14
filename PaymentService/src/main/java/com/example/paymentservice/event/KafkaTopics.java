package com.example.paymentservice.event;

public interface KafkaTopics {
    String ORDER_PLACED = "orders.placed";
    String PAYMENT_RESULT = "payment.result";
}
