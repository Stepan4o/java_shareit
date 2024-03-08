package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.StateType;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingDtoIn bookingDtoIn, long userId);

    BookingDto update(long userId, long bookingId, boolean isApprove);

    BookingDto getById(long bookingId, long userId);

    List<BookingDto> getAllByUserId(long userId, StateType state, int from, int size);

    List<BookingDto> getAllByOwnerId(long userId, StateType state, int from, int size);
}
