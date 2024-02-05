package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ItemDto {
    private Long id;
//    private Long userId;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
}
