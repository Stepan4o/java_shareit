package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.StateType;
import ru.practicum.shareit.exception.NotValidBookingDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingControllerGateway {
    private final BookingClient bookingClient;

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewBooking(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @Valid @RequestBody BookingDtoRequest bookingDtoRequest
    ) {
        LocalDateTime start = bookingDtoRequest.getStart(), end = bookingDtoRequest.getEnd();
        if (start.isAfter(end) || start.equals(end)) throw new NotValidBookingDateTime();

        log.info("POST: /bookings | userId: {}", userId);
        return bookingClient.addNewBooking(userId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @PathVariable Long bookingId,
            @RequestParam(value = "approved") Boolean isApproved
    ) {

        log.info("PATCH: /bookings/{}?approved={} | userId: {}", bookingId, isApproved, userId);
        return bookingClient.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(
            @PathVariable Long bookingId,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {

        log.info("GET: /bookings/{} | userId: {}", bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByOwnerId(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {
        StateType type = StateType.fromString(state);

        log.info("GET: /bookings/owner?state={}&from={}&size={} | userId: {}", type, from, size, userId);
        return bookingClient.getAllByOwner(userId, type, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUserId(
            @RequestParam(defaultValue = "0") @Min(0) Integer from,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestHeader(HEADER_USER_ID) Long userId
    ) {
        StateType type = StateType.fromString(state);

        log.info("GET: /bookings?state={}&from={}&size={} | userId: {}", type, from, size, userId);
        return bookingClient.getAllByUserId(userId, type, from, size);
    }
}
