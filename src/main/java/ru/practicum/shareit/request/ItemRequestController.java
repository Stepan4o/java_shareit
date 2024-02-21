package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.Constants.HEADER_USER_ID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    /** Добавление пользователем запроса с описанием вещи которая нужна */
    @PostMapping
    public ItemRequestDto addNewItemRequest(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn
    ) {

        return itemRequestService.addNewItemRequest(itemRequestDtoIn, userId);
    }

    /** Получить данные по конекретному запросу вместе с данными об ответах на него */
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @PathVariable Long requestId,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        return itemRequestService.getItemRequestByUserId(userId, requestId);
    }

    /** Получить список всех своих запросов вместе с данными об ответах на них */

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByOwnerId(
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        return itemRequestService.getItemRequestsByOwnerId(userId);
    }

    /** Просмотреть список запросов созданных другими пользователями,
     отсортированных по времени создания (от новых к старым) */
    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {

        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}
