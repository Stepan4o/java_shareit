package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDtoRequest {
    private Long id;
    @NotNull
    private Long itemId;
    @NotNull
    @FutureOrPresent(message = "Дата не может быть в прошлом")
    private LocalDateTime start;
    @NotNull
    @Future(message = "Дата может быть только в будущем")
    private LocalDateTime end;
}
