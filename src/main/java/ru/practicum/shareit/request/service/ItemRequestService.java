package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(ItemRequestDtoIn itemRequestDtoIn, Long userId);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);

    List<ItemRequestDto> getItemRequestsByOwnerId(Long userId);

    List<ItemRequestDto> getAllItemRequests(Long userId, int from, int size);
}
