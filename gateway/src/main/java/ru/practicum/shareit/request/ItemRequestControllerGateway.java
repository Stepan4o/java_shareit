package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequest;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestControllerGateway {
    private final ItemRequestClient itemRequestClient;

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewItemRequest(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Validated(Create.class) @RequestBody ItemRequestDtoRequest itemRequestDtoRequest
    ) {

        log.debug("POST: /requests | userId: {}", userId);
        return itemRequestClient.addNewItemRequest(userId, itemRequestDtoRequest);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long requestId
    ) {

        log.debug("GET: /requests/{} | userId: {}", requestId, userId);
        return itemRequestClient.getItemRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByOwnerId(
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("GET: /requests | userId: {}", userId);
        return itemRequestClient.getItemRequestsByOwnerId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {

        log.debug("GET: /requests/all?from={}&size={} | userId: {}", from, size, userId);
        return itemRequestClient.getAllItemRequests(userId, from, size);
    }
}
