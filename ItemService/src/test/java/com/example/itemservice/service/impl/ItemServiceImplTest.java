package com.example.itemservice.service.impl;



import com.example.itemservice.entity.Item;
import com.example.itemservice.repository.ItemRepository;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.ExecutableUpdate;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.UpdateWithUpdate;
import org.springframework.data.mongodb.core.ExecutableUpdateOperation.TerminatingUpdate;



import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemRepository itemRepository;     // used by get/create paths
    @Mock
    MongoTemplate mongoTemplate;       // used by availability updates

    @InjectMocks ItemServiceImpl itemService;

    private static Item item(String id, String name, BigDecimal price, int stock) {
        Item i = new Item();
        i.setId(id);
        i.setName(name);
        i.setUnitPrice(price);
        i.setAvailable(stock);
        return i;
    }
    @SuppressWarnings("unchecked")
    private TerminatingUpdate<Item> stubFluentUpdate(UpdateResult result) {
        ExecutableUpdate<Item> exec = mock(ExecutableUpdate.class);
        UpdateWithUpdate<Item> withUpdate = mock(UpdateWithUpdate.class);
        TerminatingUpdate<Item> term = mock(TerminatingUpdate.class);

        // mongoTemplate.update(Item.class) -> exec
        when(mongoTemplate.update(eq(Item.class))).thenReturn(exec);
        // exec.matching(query) -> withUpdate
        when(exec.matching(any(org.springframework.data.mongodb.core.query.Query.class)))
                .thenReturn(withUpdate);
        // withUpdate.apply(update) -> term
        when(withUpdate.apply(any(org.springframework.data.mongodb.core.query.Update.class)))
                .thenReturn(term);
        // term.first() -> UpdateResult
        when(term.first()).thenReturn(result);

        return term;
    }



    // ---------------- get by id (repository path) ----------------
    @Test
    void getItem_returnsEntity() {
        when(itemRepository.findById("123"))
                .thenReturn(Optional.of(item("123", "Pen", new BigDecimal("1.25"), 10)));

        Item resp = itemService.getItemById("123");

        assertThat(resp.getId()).isEqualTo("123");
        assertThat(resp.getName()).isEqualTo("Pen");
        assertThat(resp.getUnitPrice()).isEqualByComparingTo("1.25");
        assertThat(resp.getAvailable()).isEqualTo(10);
    }

    @Test
    void getItem_notFound_throws() {
        when(itemRepository.findById("nope")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> itemService.getItemById("nope"));
    }

    // ---------------- create (repository path) ----------------
    @Test
    void createItem_saves_andReturnsEntity() {
        Item toSave = item(null, "Notebook", new BigDecimal("4.99"), 50);

        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> {
            Item saved = inv.getArgument(0);
            saved.setId("gen-1");
            return saved;
        });

        Item resp = itemService.createItem(toSave);

        ArgumentCaptor<Item> cap = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(cap.capture());
        Item savedArg = cap.getValue();

        assertThat(savedArg.getName()).isEqualTo("Notebook");
        assertThat(savedArg.getUnitPrice()).isEqualByComparingTo("4.99");
        assertThat(savedArg.getAvailable()).isEqualTo(50);

        assertThat(resp.getId()).isEqualTo("gen-1");
    }

    @Test
    void decreaseAvailability_ok_whenEnoughStock() {
        // matched=1, modified=1
        stubFluentUpdate(UpdateResult.acknowledged(1L, 1L, null));

        itemService.decreaseAvailability("i-1", 3);

        verify(mongoTemplate).update(Item.class);
    }

    @Test
    void decreaseAvailability_throws_whenQtyNonPositive() {
        // qty = 0
        assertThrows(IllegalArgumentException.class,
                () -> itemService.decreaseAvailability("item-1", 0));

        // qty < 0
        assertThrows(IllegalArgumentException.class,
                () -> itemService.decreaseAvailability("item-1", -3));
    }


    @Test
    void increaseAvailability_addsStock() {
        stubFluentUpdate(UpdateResult.acknowledged(1L, 1L, null));

        itemService.increaseAvailability("i-2", 4);

        verify(mongoTemplate).update(Item.class);
    }

    @Test
    void increaseAvailability_throws_whenQtyNonPositive() {
        // qty = 0
        assertThrows(IllegalArgumentException.class,
                () -> itemService.increaseAvailability("item-1", 0));

        // qty < 0
        assertThrows(IllegalArgumentException.class,
                () -> itemService.increaseAvailability("item-1", -5));
    }

}
