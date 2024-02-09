package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LastBookingDto {
    private Long id;
    private Long bookerId;
}
