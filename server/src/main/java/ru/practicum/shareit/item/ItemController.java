package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.HEADER_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addNewItem(
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader(HEADER_USER_ID) long userId
    ) {
        return itemService.add(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader(HEADER_USER_ID) long userId,
            @PathVariable long itemId
    ) {
        return itemService.update(itemDtoIn, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable long itemId
    ) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySubstring(
            @RequestParam(required = false) String text,
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        return itemService.getItemsBySubstring(text, userId, from, size);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwnerId(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        return itemService.getAllItemsByOwnerId(userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestBody CommentDtoIn commentDtoIn,
            @PathVariable long itemId
    ) {
        return itemService.addComment(userId, commentDtoIn, itemId);
    }
}
