package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import java.util.List;

public interface ItemService {

    ItemDto add(ItemDtoIn itemDtoIn, Long userId);

    ItemDto update(ItemDtoIn itemDtoIn, Long itemId, Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userIdm, Integer from, Integer size);

    List<ItemDto> getItemsBySubstring(String searchText, Long userId, Integer from, Integer size);

    CommentDto addComment(Long userId, CommentDtoIn commentDtoIn, Long itemId);
}
