//package ru.practicum.shareit.request;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import ru.practicum.shareit.request.dto.ItemRequestDto;
//import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
//import ru.practicum.shareit.request.service.ItemRequestService;
//import ru.practicum.shareit.utils.Create;
//
//import javax.validation.constraints.Min;
//import java.util.List;
//
//import static ru.practicum.shareit.utils.Constants.HEADER_USER_ID;
//
//@Slf4j
//@Validated
//@RestController
//@RequiredArgsConstructor
//@RequestMapping(path = "/requests")
//public class ItemRequestController {
//
//    private final ItemRequestService itemRequestService;
//
//    /**
//     * Добавление пользователем запроса с описанием вещи которая нужна
//     */
//    @PostMapping
//    public ItemRequestDto addNewItemRequest(
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @Validated(Create.class) @RequestBody ItemRequestDtoIn itemRequestDtoIn
//    ) {
//
//        log.debug("POST: /requests | userId: {}", userId);
//        return itemRequestService.addNewItemRequest(itemRequestDtoIn, userId);
//    }
//
//    /**
//     * Получить данные по конекретному запросу вместе с данными об ответах на него
//     */
//    @GetMapping("/{requestId}")
//    public ItemRequestDto getItemRequestById(
//            @PathVariable Long requestId,
//            @RequestHeader(HEADER_USER_ID) Long userId
//    ) {
//
//        log.debug("GET: /requests/{} | userId: {}", requestId, userId);
//        return itemRequestService.getItemRequestById(userId, requestId);
//    }
//
//    /**
//     * Получить список всех своих запросов вместе с данными об ответах на них
//     */
//    @GetMapping
//    public List<ItemRequestDto> getItemRequestsByOwnerId(
//            @RequestHeader(HEADER_USER_ID) Long userId
//    ) {
//
//        log.debug("GET: /requests | userId: {}", userId);
//        return itemRequestService.getItemRequestsByOwnerId(userId);
//    }
//
//    /**
//     * Просмотреть список запросов созданных другими пользователями,
//     * отсортированных по времени создания (от новых к старым)
//     */
//    @GetMapping("/all")
//    public List<ItemRequestDto> getAllItemRequests(
//            @RequestHeader(HEADER_USER_ID) Long userId,
//            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
//            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
//    ) {
//
//        log.debug("GET: /requests/all?from={}&size={} | userId: {}", from, size, userId);
//        return itemRequestService.getAllItemRequests(userId, from, size);
//    }
//}
