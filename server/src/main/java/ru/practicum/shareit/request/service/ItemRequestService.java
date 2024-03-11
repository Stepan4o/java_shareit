package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(ItemRequestDtoIn itemRequestDtoIn, long userId);

    ItemRequestDto getItemRequestById(long userId, long requestId);

    List<ItemRequestDto> getItemRequestsByOwnerId(long userId);

    List<ItemRequestDto> getAllItemRequests(long userId, int from, int size);
}
