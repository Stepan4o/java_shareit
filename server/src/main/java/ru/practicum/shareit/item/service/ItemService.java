package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import java.util.List;

public interface ItemService {

    ItemDto add(ItemDtoIn itemDtoIn, long userId);

    ItemDto update(ItemDtoIn itemDtoIn, long itemId, long userId);

    ItemDto getItemById(long itemId, long userId);

    List<ItemDto> getAllItemsByOwnerId(long userIdm, int from, int size);

    List<ItemDto> getItemsBySubstring(String searchText, long userId, int from, int size);

    CommentDto addComment(long userId, CommentDtoIn commentDtoIn, long itemId);
}
