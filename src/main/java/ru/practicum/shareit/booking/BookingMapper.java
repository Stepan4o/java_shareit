package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public NextBookingDto toNextBookingDto(Booking booking) {
        return NextBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .build();
    }

    public LastBookingDto toLastBookingDto(Booking booking) {
        return LastBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(ItemMapper.toItemBookingDto(booking.getItem()))
                .booker(UserMapper.toBookerDto(booking.getUser().getId()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStateType())
                .build();
    }

    public List<BookingDto> toBookingsDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public Booking toBooking(BookingDtoIn bookingDtoIn) {

        LocalDateTime start = bookingDtoIn.getStart();
        LocalDateTime end = bookingDtoIn.getEnd();
        checkDateTime(start, end);

        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        return booking;
    }

    private void checkDateTime(LocalDateTime start, LocalDateTime end) {
        String message = "Дата начала бронирования должна быть раньше даты его окончания";

        if (start.isAfter(end) || start.equals(end)) {
            throw new NotAvailableException(message);
        }
    }
}
