package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.LastBookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private LastBookingDto lastBooking;
    private NextBookingDto nextBooking;
    private List<CommentDto> comments;
}
