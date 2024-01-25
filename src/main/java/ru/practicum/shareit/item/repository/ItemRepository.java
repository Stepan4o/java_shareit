package ru.practicum.shareit.item.repository;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item save(Item item);

    Item update(ItemDto itemDto, Long id, Long userId);

    Item getById(Long id);

    List<Item> findAllByUserId(Long userId);

    List<Item> findBySubstring(String text);

}
