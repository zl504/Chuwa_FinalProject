package com.example.orderservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class ItemClient {
    private final RestTemplate rt;
    public ItemClient(RestTemplate rt) { this.rt = rt; }

    public record ItemDto(String id, String name, String upc, String imageUrl, BigDecimal unitPrice, int available) {}

    public ItemDto getItem(String itemId) {
        // via Eureka + Gateway (recommended) or direct item-service port; adjust base URL as you use it
        String url = "http://localhost:8083/api/items/" + itemId; // through gateway
        return rt.getForObject(url, ItemDto.class);
    }

    public void decreaseAvailability(String itemId, int qty) {
        String url = "http://localhost:8083/api/items/" + itemId + "/availability/decrease?qty=" + qty;
        rt.put(url, null);
    }

    public void increaseAvailability(String itemId, int qty) {
        String url = "http://localhost:8083/api/items/" + itemId + "/availability/increase?qty=" + qty;
        rt.put(url, null); // PUT with no body, qty in query param
    }
}
