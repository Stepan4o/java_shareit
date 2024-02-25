package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
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
