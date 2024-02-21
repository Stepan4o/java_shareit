package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.Constants.HEADER_USER_ID;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto addNewBooking(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody BookingDtoIn bookingDtoIn
    ) {

        log.debug("POST: /bookings | userId: {}", userId);
        return service.add(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long bookingId,
            @RequestParam(value = "approved") boolean isApproved
    ) {

        log.debug("PATCH: /bookings/{}?approved={} | userId: {}", bookingId, isApproved, userId);
        return service.update(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.debug("GET: /bookings/{} | userId: {}", bookingId, userId);
        return service.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwnerId(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {

        log.debug("GET: /bookings/owner?state={} | userId: {}", state, userId);
        return service.getAllByOwnerId(userId, state, from, size);
    }

    @GetMapping
    public List<BookingDto> gelAllByUserId(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size
    ) {

        log.debug("GET: /bookings?state={} | userId: {}", state, userId);
        return service.getAllByUserId(userId, state, from, size);
    }
}
