package ru.practicum.shareit.booking.model;


import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.user.model.UserBookerDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDto {
    private Long id;

    private ItemBookingDto item;

    private UserBookerDto booker;

    @NotNull
    @FutureOrPresent(message = "Некорректно указана дата")
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent(message = "Некорректно указана дата")
    private LocalDateTime end;

    private Status status;
}
