//package com.example.paymentservice.kafka;
//
//
//import com.example.paymentservice.event.OrderPlacedEvent;
//import com.example.paymentservice.service.PaymentService;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderPlacedConsumer {
//
//    private final PaymentService payments;
//
//    public OrderPlacedConsumer(PaymentService payments) {
//        this.payments = payments;
//    }
//
//    @KafkaListener(topics = "order.placed", groupId = "payment-service")
//    public void handle(OrderPlacedEvent evt) {
//        // Charge the full order total
//        payments.charge(evt.orderId, evt.userId, evt.total);
//    }
//}
