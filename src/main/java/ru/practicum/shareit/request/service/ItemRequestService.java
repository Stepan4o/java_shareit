package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(ItemRequestDtoIn itemRequestDtoIn, Long userId);
    List<ItemRequestDto> getItemRequestsByOwnerId(Long userId);
}
