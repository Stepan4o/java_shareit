package ru.practicum.shareit.item.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ItemDtoIn {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private boolean available;
}
