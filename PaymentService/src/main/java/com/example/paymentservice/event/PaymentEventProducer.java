package com.example.paymentservice.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class PaymentEventProducer {
    private final KafkaTemplate<String, Object> kafka;

    public PaymentEventProducer(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    public void publishPaymentResult(PaymentResultEvent evt) {
        kafka.send(KafkaTopics.PAYMENT_RESULT, evt.getOrderId().toString(), evt);
    }
}
