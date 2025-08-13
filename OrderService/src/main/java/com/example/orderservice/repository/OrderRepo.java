package com.example.orderservice.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import com.example.orderservice.domain.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends CassandraRepository<Order, UUID> {
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
}
