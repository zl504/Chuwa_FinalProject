package com.example.orderservice.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import com.example.orderservice.domain.Order;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepo extends CassandraRepository<Order, UUID> {
    @Query("SELECT * FROM orders WHERE user_id = ?0 ALLOW FILTERING")
    List<Order> findByUserIdAllowFiltering(Long userId);
}
