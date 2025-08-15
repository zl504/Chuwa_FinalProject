package com.example.itemservice.service.impl;

import com.example.itemservice.entity.Item;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.service.ItemService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final MongoTemplate mongoTemplate;


    public ItemServiceImpl(ItemRepository itemRepository, MongoTemplate mongoTemplate) {
        this.itemRepository = itemRepository;
        this.mongoTemplate = mongoTemplate;
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

    /**
     * Atomically decrease availability if enough stock exists.
     * @return true if decremented; false if not enough stock.
     */
    @Override
    public boolean decreaseAvailability(String id, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");
        Query q = new Query(Criteria.where("_id").is(id).and("available").gte(qty));
        Update u = new Update().inc("available", -qty);
        UpdateResult res = mongoTemplate.update(Item.class).matching(q).apply(u).first();
        return res.getModifiedCount() == 1;
    }

    /**
     * Atomically increase availability (e.g., on cancel/refund).
     */
    @Override
    public void increaseAvailability(String id, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be positive");
        Query q = new Query(Criteria.where("_id").is(id));
        Update u = new Update().inc("available", qty);
        mongoTemplate.update(Item.class).matching(q).apply(u).first();
    }


}
