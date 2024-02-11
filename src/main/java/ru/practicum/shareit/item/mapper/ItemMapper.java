package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemMapper {

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .comments(List.of())
                .build();
    }
    public ItemDto toItemWithBookingDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .nextBooking(null)
                .lastBooking(null)
                .comments(List.of())
                .build();
    }

    public List<ItemDto> toItemsDto(List<Item> items) {
        List<ItemDto> res = new ArrayList<>();
        for (Item item : items) {
            res.add(toItemDto(item));
        }
        return res;
    }

    public Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public ItemBookingDto toItemBookingDto(Item item) {
        return ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

//    public ItemWithLastBookingDto toItemWithLastBookingDto(Item item) {
//        return ItemWithLastBookingDto.builder()
//                .id(item.getId())
//                .name(item.getName())
//                .description(item.getDescription())
//                .lastBooking(null)
//                .nextBooking(null)
//                .build();
//    }
}
