package com.example.orderservice.controller;


import com.example.orderservice.Service.impl.OrderServiceImpl;
import com.example.orderservice.client.AccountClient;
import com.example.orderservice.payload.OrderResponse;
import com.example.orderservice.payload.PlaceOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final AccountClient accountClient;


    public OrderController(OrderServiceImpl orderService, AccountClient accountClient) {
        this.orderService = orderService;
        this.accountClient = accountClient;
    }
    @PostMapping
    public ResponseEntity<OrderResponse> place(

            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth,
            @RequestBody PlaceOrderRequest body) {

        // basic guard
//        if (auth == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        // Ask Account service who this token represents
        var me = accountClient.me(auth); // expects 200 with { id, username, ... }

        // Pass body + user id into service (ignore any client-sent userId)
        var resp = orderService.place(body, me.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<OrderResponse>> byUser(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.listByUser(userId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        orderService.cancel(id);
        return ResponseEntity.noContent().build();
    }

}
