package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoIn {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
