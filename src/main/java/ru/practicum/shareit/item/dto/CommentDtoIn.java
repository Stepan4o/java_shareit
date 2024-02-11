package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDtoIn {
    @NotEmpty
    private String text;
    private LocalDateTime create = LocalDateTime.now();
}
