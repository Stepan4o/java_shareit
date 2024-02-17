package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        if (item.getItemRequest() != null) {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.isAvailable())
                    .requestId(item.getItemRequest().getId())
                    .build();
        } else {
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.isAvailable())
                    .build();
        }
    }

    public List<ItemDto> toItemsDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public Item toItem(ItemDtoIn itemDtoIn) {
        Item item = new Item();
        item.setName(itemDtoIn.getName());
        item.setDescription(itemDtoIn.getDescription());
        item.setAvailable(itemDtoIn.getAvailable());
        return item;
    }

    public ItemBookingDto toItemBookingDto(Item item) {
        return ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}
