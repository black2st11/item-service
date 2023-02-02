package hello.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {
    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach(){
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);
        //when
        Item savedItem = itemRepository.save(item);
        //then
        Item findItem = itemRepository.findById(savedItem.getId());

        assertThat(item).isEqualTo(savedItem);
        assertThat(savedItem).isEqualTo(findItem);
    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("Item1", 10000, 10);
        Item item2 = new Item("Item2", 20000, 20);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> items = itemRepository.findAll();

        //then
        assertThat(items.size()).isEqualTo(2);
        assertThat(items).contains(item1, item2);
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("ItemA", 10000, 10);
        itemRepository.save(item);

        //when
        Item updateItemParam = new Item("ItemB", 20000, 20);
        itemRepository.update(item.getId(), updateItemParam);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem.getItemName()).isEqualTo(updateItemParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateItemParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateItemParam.getQuantity());
    }
}
