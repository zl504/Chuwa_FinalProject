package com.example.orderservice.event;

import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.repository.OrderRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class PaymentResultConsumer {
    private final OrderRepo orders;

    public PaymentResultConsumer(OrderRepo orders) {
        this.orders = orders;
    }

    @KafkaListener(
            topics = KafkaTopics.PAYMENT_RESULT,
            groupId = "order-service",
            containerFactory = "paymentResultKafkaFactory"
    )
    public void handle(PaymentResultEvent evt) {
        var order = orders.findById(evt.getOrderId()).orElse(null);
        if (order == null) return;

        if (evt.getStatus() == PaymentResultEvent.PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.PAID.name());
        } else {
            order.setStatus(OrderStatus.CANCELLED.name());
            // (optional) compensate stock here if you didn’t do it on cancel endpoint
        }
        orders.save(order);
    }
}
