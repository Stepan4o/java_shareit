package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoIn {
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent(message = "Некорректно указана дата")
    private LocalDateTime start;

    @NotNull
    @Future(message = "Некорректно указана дата")
    private LocalDateTime end;
}
