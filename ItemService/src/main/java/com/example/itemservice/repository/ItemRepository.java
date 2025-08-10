package com.example.itemservice.repository;
import com.example.itemservice.entity.Item;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ItemRepository extends MongoRepository<Item, String>{

}
