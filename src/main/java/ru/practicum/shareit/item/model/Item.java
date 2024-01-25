package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private boolean available;
}
