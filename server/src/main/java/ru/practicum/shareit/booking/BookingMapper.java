package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public BookingDto.NextBookingDto toNextBookingDto(Booking booking) {
        return BookingDto.NextBookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .build();
    }

    public BookingDto.LastBookingDto toLastBookingDto(Booking booking) {
        return BookingDto.LastBookingDto.builder()
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
        Booking booking = new Booking();
        booking.setStart(bookingDtoIn.getStart());
        booking.setEnd(bookingDtoIn.getEnd());
        return booking;
    }
}
