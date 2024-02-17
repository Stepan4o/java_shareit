package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.model.ItemRequest;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequest toItemRequest(ItemRequestDtoIn itemRequestDtoIn) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDtoIn.getDescription());
        itemRequest.setCreated(itemRequestDtoIn.getCreation());

        return itemRequest;
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, Long userId) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .userId(userId)
                .created(itemRequest.getCreated())
                .build();
    }
}
