package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentDtoIn {
    @NotBlank(groups = {Create.class})
    @Size(max = 1000, groups = {Create.class, Update.class})
    private String text;
    private LocalDateTime create = LocalDateTime.now();
}
