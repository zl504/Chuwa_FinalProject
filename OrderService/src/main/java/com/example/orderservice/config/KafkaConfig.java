package com.example.orderservice.config;


import com.example.orderservice.event.PaymentResultEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaConfig {

    // Producer (String key, JSON value)
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(
                Map.of(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                        org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class,
                        org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class));
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Consumer for PaymentResultEvent
    @Bean
    public ConsumerFactory<String, PaymentResultEvent> paymentResultConsumerFactory() {
        // Delegate JSON deserializer bound to your local DTO
        var json = new org.springframework.kafka.support.serializer.JsonDeserializer<>(
                com.example.orderservice.event.PaymentResultEvent.class
        );
        json.addTrustedPackages("*");
        // IMPORTANT: ignore any type headers on the record
        json.setUseTypeHeaders(false);         // don't honor __TypeId__
        // (optional) strip headers so they don't leak forward
        json.setRemoveTypeHeaders(true);

        // Wrap the JSON deserializer so DefaultErrorHandler can handle SerializationException
        var errorHandling = new org.springframework.kafka.support.serializer.ErrorHandlingDeserializer<>(json);

        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(
                Map.of(
                        org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                        org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class,
                        org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.ErrorHandlingDeserializer.class,
                        org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "order-service"
                ),
                new org.apache.kafka.common.serialization.StringDeserializer(),
                errorHandling
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentResultEvent> paymentResultKafkaFactory() {
        var f = new ConcurrentKafkaListenerContainerFactory<String, PaymentResultEvent>();
        f.setConsumerFactory(paymentResultConsumerFactory());
        return f;
    }
}
