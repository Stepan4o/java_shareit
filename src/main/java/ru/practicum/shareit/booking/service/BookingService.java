package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingValidationDto;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingValidationDto bookingValidationDto, Long userId);
    BookingDto patchUpdate(Long userId, Long bookingId, boolean bool);
    BookingDto getById(Long bookingId, Long userId);
    List<BookingDto> getAllByUserId(Long userId, String state);
    List<BookingDto> getAllByOwnerId(Long userId, String state);

}
