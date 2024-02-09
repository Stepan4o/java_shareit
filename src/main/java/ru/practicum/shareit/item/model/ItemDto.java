package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.LastBookingDto;
import ru.practicum.shareit.booking.model.NextBookingDto;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Transient
    private LastBookingDto lastBooking;
    @Transient
    private NextBookingDto nextBooking;

}
