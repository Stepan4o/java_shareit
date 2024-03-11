package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class ItemRequestDtoRequest {
    @NotBlank(groups = {Create.class})
    @Size(max = 1000, groups = {Create.class, Update.class})
    private String description;
    private LocalDateTime creation = LocalDateTime.now();
    private Long requestId;
}
