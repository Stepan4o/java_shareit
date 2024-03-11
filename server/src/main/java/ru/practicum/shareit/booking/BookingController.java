package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.utils.Constants.HEADER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto addNewBooking(
            @RequestHeader(HEADER_USER_ID) long userId,
            @RequestBody BookingDtoIn bookingDtoIn
    ) {
        return service.add(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader(HEADER_USER_ID) long userId,
            @PathVariable long bookingId,
            @RequestParam(value = "approved") boolean isApproved
    ) {
        return service.update(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(HEADER_USER_ID) long userId
    ) {
        return service.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerId(
            @RequestParam int from,
            @RequestParam int size,
            @RequestParam StateType state,
            @RequestHeader(HEADER_USER_ID) long userId
    ) {
        return service.getAllByOwnerId(userId, state, from, size);
    }

    @GetMapping
    public List<BookingDto> getAllByUserId(
            @RequestParam int from,
            @RequestParam int size,
            @RequestParam StateType state,
            @RequestHeader(HEADER_USER_ID) long userId

    ) {
        return service.getAllByUserId(userId, state, from, size);
    }
}
