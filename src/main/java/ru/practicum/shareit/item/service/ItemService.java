package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDtoIn itemDtoIn, Long userId);

    ItemDto patchUpdateItem(ItemDtoIn itemDtoIn, Long id, Long userId);

    ItemDto getItemById(Long id, Long userId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> getItemsBySubstring(String searchText, Long userId);

    CommentDto addComment(Long userId, CommentDtoIn commentDtoIn, Long itemId);
}
