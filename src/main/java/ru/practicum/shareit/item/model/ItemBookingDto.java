package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemBookingDto {
    private Long id;
    private String name;
}
