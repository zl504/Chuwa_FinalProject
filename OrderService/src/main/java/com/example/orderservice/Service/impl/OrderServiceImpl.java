package com.example.orderservice.Service.impl;




import com.example.orderservice.Service.OrderService;
import com.example.orderservice.client.ItemClient;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderLineUdt;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.event.OrderEventProducer;
import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.payload.OrderResponse;
import com.example.orderservice.payload.PlaceOrderRequest;
import com.example.orderservice.repository.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepo repo;
    private final ItemClient items;
    private final OrderEventProducer events;

    public OrderServiceImpl(OrderRepo repo, ItemClient items, OrderEventProducer events) {
        this.repo = repo; this.items = items; this.events = events;
    }

    // OrderServiceImpl.java (excerpt)
    @Override
    @Transactional
    public OrderResponse place(PlaceOrderRequest req, Long userId) {
        var order = new Order();
        order.setId(UUID.randomUUID());
        order.setUserId(userId);

        for (var line : req.getItems()) {
            var item = items.getItem(line.getItemId());
            if (item.available() < line.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for item " + item.name());
            }
            // reserve/decrease stock
            items.decreaseAvailability(item.id(), line.getQuantity());

            order.addLine(OrderLineUdt.of(
                    item.id(),
                    item.name(),
                    item.unitPrice(),
                    line.getQuantity()
            ));
        }

        order = repo.save(order);

        var evtLines = order.getLines().stream()
                .map(l -> new OrderPlacedEvent.Line(l.getItemId(), l.getQuantity(), l.getUnitPrice()))
                .toList();
        events.publishOrderPlaced(new OrderPlacedEvent(
                order.getId(), order.getUserId(), order.getTotal(), order.getCreatedAt(), evtLines
        ));

        return toResponse(order);
    }

    @Override
    public OrderResponse get(UUID id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Override
    public List<OrderResponse> listByUser(Long userId) {
        return repo.findByUserIdAllowFiltering(userId).stream()
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void cancel(UUID id) {
        var order = repo.findById(id).orElseThrow();
        if (!OrderStatus.CANCELLED.name().equals(order.getStatus())) {
            order.setStatus(OrderStatus.CANCELLED.name());
            repo.save(order);
            // (Optional) publish OrderCancelled and compensate inventory by increasing availability
        }
    }


    private OrderResponse toResponse(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getUserId(),
                o.getTotal(),
                OrderStatus.valueOf(o.getStatus()),
                o.getCreatedAt(),
                o.getLines().stream()
                        .map(l -> new OrderResponse.Line(
                                l.getItemId(),
                                l.getItemName(),
                                l.getUnitPrice(),
                                l.getQuantity(),
                                l.getLineTotal()))
                        .toList()
        );
    }
}
