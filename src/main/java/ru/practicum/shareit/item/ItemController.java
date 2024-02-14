package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(
            @Valid
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("POST: /items | userId: {}", userId);
        return itemService.createItem(itemDtoIn, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto patchUpdateItem(
            @RequestBody ItemDtoIn itemDtoIn,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long id
    ) {

        log.debug("PATCH: /items/{} | userId: {}", id, userId);
        return itemService.patchUpdateItem(itemDtoIn, id, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long id
    ) {

        log.debug("GET: /items/{} | userId: {}", id, userId);
        return itemService.getItemById(id, userId);
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
    public CommentDto addComment(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody CommentDtoIn commentDtoIn,
            @PathVariable Long itemId
    ) {
        log.debug("POST: /items/{}/comment | userId: {}", itemId, userId);
        return itemService.addComment(userId, commentDtoIn, itemId);
    }
}
