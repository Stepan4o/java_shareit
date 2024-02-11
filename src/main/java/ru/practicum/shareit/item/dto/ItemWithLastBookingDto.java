package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.LastBookingDto;
import ru.practicum.shareit.booking.model.NextBookingDto;

@Data
@Builder
public class ItemWithLastBookingDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private LastBookingDto lastBooking;
    private NextBookingDto nextBooking;
}
