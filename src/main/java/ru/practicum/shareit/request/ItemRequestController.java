package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto addNewItemRequest(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn

    ) {

        return itemRequestService.addNewItemRequest(itemRequestDtoIn, userId);
    }

    // TODO изменить на byUserId
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestByOwnerId(
            @PathVariable Long requestId,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        return itemRequestService.getItemRequestByOwnerId(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequestsByOwnerId(
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        return itemRequestService.getItemRequestsByOwnerId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {

        return itemRequestService.getAllItemRequests(userId, from, size);
    }
}
