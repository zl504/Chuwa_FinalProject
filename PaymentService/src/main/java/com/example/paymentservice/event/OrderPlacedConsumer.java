package com.example.paymentservice.event;


import com.example.paymentservice.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPlacedConsumer {
    private final PaymentService payments;

    public OrderPlacedConsumer(PaymentService payments) {
        this.payments = payments;
    }

    @KafkaListener(
            topics = KafkaTopics.ORDER_PLACED,
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "orderPlacedKafkaFactory"
    )
    public void handle(OrderPlacedEvent evt) {
        payments.charge(evt.getOrderId(), evt.getUserId(), evt.getTotal());
    }
}
