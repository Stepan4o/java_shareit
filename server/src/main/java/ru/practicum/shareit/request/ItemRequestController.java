package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.HEADER_USER_ID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto addNewItemRequest(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestBody ItemRequestDtoIn itemRequestDtoIn
    ) {
        return itemRequestService.addNewItemRequest(itemRequestDtoIn, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(
            @PathVariable long requestId,
            @RequestHeader(HEADER_USER_ID) long userId
    ) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByOwnerId(
            @RequestHeader(HEADER_USER_ID) long userId
    ) {
        return itemRequestService.getItemRequestsByOwnerId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestParam int from,
            @RequestParam int size
    ) {
        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}
