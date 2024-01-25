package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto patchUpdateItem(ItemDto itemDto, Long id, Long userId);

    ItemDto getItemById(Long id);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> getItemsBySubstring(String text);
}
