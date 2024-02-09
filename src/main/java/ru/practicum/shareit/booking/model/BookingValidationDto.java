package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingValidationDto {
    private Long id;

    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent(message = "Некорректно указана дата")
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent(message = "Некорректно указана дата")
    private LocalDateTime end;

}
