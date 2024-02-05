package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto patchUpdateItem(ItemDto itemDto, Long id, Long userId);

    ItemDto getItemById(Long id, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> getItemsBySubstring(String searchText, Long userId);
}
