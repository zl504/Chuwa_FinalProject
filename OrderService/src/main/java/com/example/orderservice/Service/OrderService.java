package com.example.orderservice.Service;

import com.example.orderservice.payload.OrderResponse;
import com.example.orderservice.payload.PlaceOrderRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    /**
     * Place a new order for the authenticated user.
     * @param req    request payload containing line items
     * @param userId authenticated user's id (resolved from token)
     * @return created order as response DTO
     */
    OrderResponse place(PlaceOrderRequest req, Long userId);

    /**
     * Get a single order by id.
     */
    OrderResponse get(UUID id);

    /**
     * List orders for a user, most recent first.
     */
    List<OrderResponse> listByUser(Long userId);

    /**
     * Cancel an order (idempotent).
     */
    void cancel(UUID id);

}
