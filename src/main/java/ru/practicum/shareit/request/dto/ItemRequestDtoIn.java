package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoIn {
    @NotBlank
    private String description;
    private LocalDateTime creation = LocalDateTime.now();
    private Long requestId;
}
