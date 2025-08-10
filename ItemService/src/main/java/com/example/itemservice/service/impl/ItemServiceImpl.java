package com.example.itemservice.service.impl;

import com.example.itemservice.entity.Item;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.service.ItemService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItemById(String id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(String id, Item updatedItem) {
        Item existing = getItemById(id);
        existing.setName(updatedItem.getName());
        existing.setUpc(updatedItem.getUpc());
        existing.setImageUrl(updatedItem.getImageUrl());
        existing.setUnitPrice(updatedItem.getUnitPrice());
        existing.setAvailable(updatedItem.getAvailable());
        existing.setMetadata(updatedItem.getMetadata());
        return itemRepository.save(existing);
    }

    @Override
    public void deleteItem(String id) {
        itemRepository.deleteById(id);
    }

    @Override
    public int getAvailability(String id) {
        return itemRepository.findById(id)
                .map(Item::getAvailable)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

}
