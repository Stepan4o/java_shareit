package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemControllerGateway {
    private final ItemClient itemClient;

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewItem(
            @Validated(Create.class) @RequestBody ItemDtoRequest itemDtoRequest,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("POST: /items | userId: {}", userId);
        return itemClient.addNewItem(userId, itemDtoRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(
            @Validated(Update.class) @RequestBody ItemDtoRequest itemDtoRequest,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long itemId
    ) {

        log.debug("PATCH: /items/{} | userId: {}", itemId, userId);
        return itemClient.updateItem(itemDtoRequest, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @PathVariable Long itemId,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("GET: /items/{} | userId: {}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(
            @RequestParam(required = false) String text,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size
    ) {

        log.debug("GET: /items/search?searchText={}&from={}&size={} | userId: {}", text, from, size, userId);
        return text.isBlank() ? ResponseEntity.ok(List.of()) : itemClient.getItemByText(userId, text, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUserId(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size
    ) {

        log.debug("GET: /items?from={}&size={} | userId: {}", from, size, userId);
        return itemClient.getAllItemsByUserId(userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addCommentToItem(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Validated(Create.class) @RequestBody CommentDtoRequest commentDtoRequest,
            @PathVariable Long itemId
    ) {

        log.debug("POST: /items/{}/comment | userId: {}", itemId, userId);
        return itemClient.addCommentToItem(userId, itemId, commentDtoRequest);
    }
}
