package com.example.orderservice.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;



@Component
public class OrderEventProducer {
    private final KafkaTemplate<String, Object> kafka;
    public OrderEventProducer(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publishOrderPlaced(OrderPlacedEvent event) {
        kafka.send(KafkaTopics.ORDER_PLACED, event.getOrderId().toString(), event);
    }
}
