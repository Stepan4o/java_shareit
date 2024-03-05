//package ru.practicum.shareit.item;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.shareit.item.dto.CommentDto;
//import ru.practicum.shareit.item.dto.CommentDtoIn;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.ItemDtoIn;
//import ru.practicum.shareit.item.service.ItemService;
//import ru.practicum.shareit.utils.Create;
//import ru.practicum.shareit.utils.Update;
//
//import javax.validation.constraints.Max;
//import javax.validation.constraints.Min;
//import java.util.List;
//
//import static ru.practicum.shareit.utils.Constants.HEADER_USER_ID;
//
//@Slf4j
//@Validated
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/items")
//public class ItemController {
//
//    private final ItemService itemService;
//
//    /**
//     * По эндпоинту два сценария. Обычное добавление вещи, или же добавление
//     * к соответсвующему запросу по requestId (указанному в теле itemDtoIn.requestId)
//     * созданному другим пользователем по желаемой вещи
//     */
//    @PostMapping
//    public ItemDto addNewItem(
//            @Validated(Create.class) @RequestBody ItemDtoIn itemDtoIn,
//            @RequestHeader(HEADER_USER_ID) Long userId
//    ) {
//
//        log.debug("POST: /items | userId: {}", userId);
//        return itemService.add(itemDtoIn, userId);
//    }
//
//    @PatchMapping("/{itemId}")
//    public ItemDto updateItem(
//            @Validated(Update.class) @RequestBody ItemDtoIn itemDtoIn,
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @PathVariable Long itemId
//    ) {
//
//        log.debug("PATCH: /items/{} | userId: {}", itemId, userId);
//        return itemService.update(itemDtoIn, itemId, userId);
//    }
//
//    @GetMapping("/{itemId}")
//    public ItemDto getItemById(
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @PathVariable Long itemId
//    ) {
//
//        log.debug("GET: /items/{} | userId: {}", itemId, userId);
//        return itemService.getItemById(itemId, userId);
//    }
//
//    @GetMapping("/search")
//    public List<ItemDto> getItemsBySubstring(
//            @RequestParam(required = false) String text,
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @RequestParam(defaultValue = "0") @Min(0) Integer from,
//            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size
//    ) {
//
//        log.debug("GET: /items/search?searchText={}&from={}&size={} | userId: {}", text, from, size, userId);
//        return itemService.getItemsBySubstring(text, userId, from, size);
//    }
//
//    /**
//     * Если запрашивает владелец, вернуть с комментрариями пользователей
//     * и последними/следующими бронированиями. Иначле только с комметариями
//     */
//    @GetMapping
//    public List<ItemDto> getAllItemsByUserId(
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @RequestParam(defaultValue = "0") @Min(0) Integer from,
//            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size
//    ) {
//
//        log.debug("GET: /items?from={}&size={} | userId: {}", from, size, userId);
//        return itemService.getAllItemsByUserId(userId, from, size);
//    }
//
//    /**
//     * Комметраий можно оставить толко пользователю который брал вещь в аренду
//     */
//    @PostMapping("/{itemId}/comment")
//    public CommentDto addCommentToItem(
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @Validated(Create.class) @RequestBody CommentDtoIn commentDtoIn,
//            @PathVariable Long itemId
//    ) {
//        log.debug("POST: /items/{}/comment | userId: {}", itemId, userId);
//        return itemService.addComment(userId, commentDtoIn, itemId);
//    }
//}
