package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingService service;

    @PostMapping
    public BookingDto add(@RequestHeader(HEADER_USER_ID) Long userId,
                          @Valid
                          @RequestBody BookingDtoIn bookingDtoIn) {

        return service.add(bookingDtoIn, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto patchUpdate(@RequestHeader(HEADER_USER_ID) Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam(value = "approved") boolean bool) {
        return service.patchUpdate(userId, bookingId, bool);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId,
                              @RequestHeader(HEADER_USER_ID) Long userId) {
        return service.getById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        return service.getAllByOwnerId(userId, state);
    }

    @GetMapping
    public List<BookingDto> gelAllByUserId(
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        return service.getAllByUserId(userId, state);
    }
}
