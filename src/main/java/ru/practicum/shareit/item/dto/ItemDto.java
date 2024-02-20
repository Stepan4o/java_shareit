package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.LastBookingDto;
import ru.practicum.shareit.booking.dto.NextBookingDto;

import java.util.List;

@Builder
@Getter
@Setter
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private LastBookingDto lastBooking;
    private NextBookingDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
