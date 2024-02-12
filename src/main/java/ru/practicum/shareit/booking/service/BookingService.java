package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingDtoIn bookingDtoIn, Long userId);

    BookingDto patchUpdate(Long userId, Long bookingId, boolean bool);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getAllByUserId(Long userId, String state);

    List<BookingDto> getAllByOwnerId(Long userId, String state);
}
