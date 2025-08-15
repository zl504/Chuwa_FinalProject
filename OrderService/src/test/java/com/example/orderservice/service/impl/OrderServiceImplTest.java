package com.example.orderservice.service.impl;


import com.example.orderservice.Service.impl.OrderServiceImpl;
import com.example.orderservice.client.ItemClient;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderLineUdt;
import com.example.orderservice.domain.OrderStatus;
import com.example.orderservice.event.OrderEventProducer;
import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.payload.OrderResponse;
import com.example.orderservice.payload.PlaceOrderRequest;
import com.example.orderservice.repository.OrderRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock OrderRepo repo;
    @Mock ItemClient items;
    @Mock OrderEventProducer events;

    @InjectMocks OrderServiceImpl service;


    private static Order newOrder(UUID id, Long userId, Instant createdAt, String status, List<OrderLineUdt> lines, BigDecimal total) {
        Order o = new Order();
        o.setId(id);
        o.setUserId(userId);
        o.setCreatedAt(createdAt);
        o.setStatus(status);
        o.setLines(lines);
        o.setTotal(total);
        return o;
    }
    // ========== get() ==========

    @Test
    void get_returnsMappedOrderResponse_whenFound() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000123");

        var line = OrderLineUdt.of("I-1", "Pen", new BigDecimal("1.50"), 2);
        var order = newOrder(
                id,
                1001L,
                Instant.parse("2025-01-01T10:00:00Z"),
                OrderStatus.PENDING.name(),
                List.of(line),
                new BigDecimal("3.00")
        );

        when(repo.findById(id)).thenReturn(Optional.of(order));

        OrderResponse resp = service.get(id);

        assertThat(resp.getId()).isEqualTo(id);
        assertThat(resp.getUserId()).isEqualTo(1001L);
        assertThat(resp.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(resp.getTotal()).isEqualByComparingTo("3.00");
        assertThat(resp.getLines()).hasSize(1);
        assertThat(resp.getLines().get(0).getItemId()).isEqualTo("I-1");
    }

    @Test
    void get_throws_whenNotFound() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000999");
        when(repo.findById(id)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.get(id));
    }

    // ========== listByUser() ==========

    @Test
    void listByUser_returnsNewestFirst() {
        Long userId = 42L;
        var older = newOrder(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                userId,
                Instant.parse("2025-01-01T09:00:00Z"),
                OrderStatus.PENDING.name(),
                List.of(),
                BigDecimal.ZERO
        );
        var newer = newOrder(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                userId,
                Instant.parse("2025-01-01T12:00:00Z"),
                OrderStatus.PENDING.name(),
                List.of(),
                BigDecimal.ZERO
        );

        when(repo.findByUserIdAllowFiltering(userId)).thenReturn(List.of(older, newer));

        List<OrderResponse> out = service.listByUser(userId);

        assertThat(out).hasSize(2);
        assertThat(out.get(0).getId()).isEqualTo(newer.getId());
        assertThat(out.get(1).getId()).isEqualTo(older.getId());
    }

    // ========== cancel() ==========

    @Test
    void cancel_returnsFalse_whenAlreadyCancelled_andDoesNothing() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000010");
        var line = OrderLineUdt.of("I-1", "Pen", new BigDecimal("1.50"), 2);
        var order = newOrder(id, 7L, Instant.now(), OrderStatus.CANCELLED.name(), List.of(line), new BigDecimal("3.00"));

        when(repo.findById(id)).thenReturn(Optional.of(order));

        boolean changed = service.cancel(id);

        assertThat(changed).isFalse();
        verify(items, never()).increaseAvailability(anyString(), anyInt());
        verify(repo, never()).save(any(Order.class));
    }

    @Test
    void cancel_returnsTrue_whenPending_compensatesInventory_andSaves() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000011");
        var l1 = OrderLineUdt.of("I-1", "Pen", new BigDecimal("1.50"), 2);
        var l2 = OrderLineUdt.of("I-2", "Notebook", new BigDecimal("4.00"), 1);
        var pending = newOrder(id, 9L, Instant.now(), OrderStatus.PENDING.name(), List.of(l1, l2), new BigDecimal("7.00"));

        when(repo.findById(id)).thenReturn(Optional.of(pending));
        when(repo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        boolean changed = service.cancel(id);

        assertThat(changed).isTrue();

        // inventory compensation for BOTH lines
        verify(items).increaseAvailability("I-1", 2);
        verify(items).increaseAvailability("I-2", 1);

        // status saved as CANCELLED
        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(repo).save(cap.capture());
        assertThat(cap.getValue().getStatus()).isEqualTo(OrderStatus.CANCELLED.name());
        assertThat(cap.getValue().getId()).isEqualTo(id);
    }
}
