package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class NotValidBookingDateTime extends RuntimeException {
    private final String message = "Дата начала бронирования должна быть раньше даты его окончания";
}
