package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String USER_ID = "X-Sharer-User-Id";

    @Autowired
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(
            @Valid
            @RequestBody ItemDto itemDto,
            @RequestHeader(USER_ID) Long userId
    ) {

        log.debug("POST: /items ownerId:{}", userId);
        return itemService.addItem(itemDto, userId);
    }

    @PatchMapping("/{id}")
    public ItemDto patchUpdate(
            @RequestBody ItemDto itemDto,
            @RequestHeader(USER_ID) Long userId,
            @PathVariable Long id
    ) {

        log.debug("PATCH: /items/{} ownerId:{}", id, userId);
        return itemService.patchUpdateItem(itemDto, id, userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(
            @RequestHeader(value = USER_ID, required = false) Long userId,
            @PathVariable Long id
    ) {
        /*
        Не очень понял как в рамках этого ТЗ в данном методе
        использовать приходящий userId. Ведь просматривать вещь по itemId
        может любой пользователь. Или это "некая проверка"
        на то, авторизован ли пользователь или нет?
        На время первой отправки поставил required=false
        Такая же ситуация в методе ниже. Буду рад комментарию, сделаю как нужно :)
        */
        log.debug("GET: /items/{}", id);
        return itemService.getItemById(id);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySubstring(
            @RequestParam(required = false) String text,
            @RequestHeader(value = USER_ID, required = false) Long userId
    ) {

        log.debug("GET: /items/search?text={}", text);
        if (text == null || text.isBlank()) {
            return List.of();
        } else {
            return itemService.getItemsBySubstring(text.toLowerCase());
        }
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUserId(
            @RequestHeader(USER_ID) Long userId
    ) {

        log.debug("GET: /items ownerId:{}", userId);
        return itemService.getAllItemsByUserId(userId);
    }
}
