package com.example.itemservice.controller;


import com.example.itemservice.entity.Item;
import com.example.itemservice.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public Item getItemById(@PathVariable String id) {
        return itemService.getItemById(id);
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemService.updateItem(id, item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<Integer> getItemAvailability(@PathVariable String id) {
        int availableUnits = itemService.getAvailability(id);
        return ResponseEntity.ok(availableUnits);
    }
}
