package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;


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

    @GetMapping
    public List<ItemRequestDto> getItemRequestByOwnerId(
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {
    return itemRequestService.getItemRequestsByOwnerId(userId);
    }
}
