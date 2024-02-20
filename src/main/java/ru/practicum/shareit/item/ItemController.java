package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoIn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto addNewItem(
            @Valid @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("POST: /items | userId: {}", userId);
        return itemService.add(itemDtoIn, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long itemId
    ) {

        log.debug("PATCH: /items/{} | userId: {}", itemId, userId);
        return itemService.update(itemDtoIn, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long itemId
    ) {

        log.debug("GET: /items/{} | userId: {}", itemId, userId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySubstring(
            @RequestParam(required = false) String text,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("GET: /items/search?searchText={} | userId: {}", text, userId);
        return itemService.getItemsBySubstring(text, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("GET: /items | userId:{}", userId);
        return itemService.getAllItemsByUserId(userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody CommentDtoIn commentDtoIn,
            @PathVariable Long itemId
    ) {
        log.debug("POST: /items/{}/comment | userId: {}", itemId, userId);
        return itemService.addComment(userId, commentDtoIn, itemId);
    }
}
