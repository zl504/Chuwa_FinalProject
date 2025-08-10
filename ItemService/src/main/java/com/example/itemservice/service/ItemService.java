package com.example.itemservice.service;

import com.example.itemservice.entity.Item;

import java.util.List;

public interface ItemService {
    List<Item> getAllItems();
    Item getItemById(String id);
    Item createItem(Item item);
    Item updateItem(String id, Item updatedItem);
    void deleteItem(String id);
    int getAvailability(String id);
}
