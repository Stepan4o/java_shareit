package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto add(BookingDtoIn bookingDtoIn, long userId) {
        Item savedItem = itemRepository.findById(bookingDtoIn.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format(
                        ITEM_NOT_FOUND, bookingDtoIn.getItemId()
                )));
        if (savedItem.isAvailable()) {
            if (!isCoincidence(savedItem.getUser().getId(), userId)) {
                User booker = userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException(String.format(
                                USER_NOT_FOUND, userId
                        )));
                Booking booking = BookingMapper.toBooking(bookingDtoIn);
                booking.setItem(savedItem);
                booking.setUser(booker);
                return BookingMapper.toBookingDto(repository.save(booking));
            } else {
                throw new NotFoundException("Невозможно забронировать свою вещь");
            }
        } else {
            throw new NotAvailableException("Вещь не доступна для бронирования");
        }
    }

    @Override
    public BookingDto update(long userId, long bookingId, boolean isApproved) {
        Booking booking = repository.findBookingByIdAndItemUserId(bookingId, userId).orElseThrow(
                () -> new NotFoundException(
                        String.format(BOOKING_NOT_FOUND, bookingId)
                ));
        if (booking.getStateType().equals(StateType.WAITING)) {
            StateType status = isApproved ? StateType.APPROVED : StateType.REJECTED;
            booking.setStateType(status);
        } else {
            throw new NotAvailableException(IS_NOT_WAITING);
        }
        return BookingMapper.toBookingDto(repository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(long bookingId, long userId) {
        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(
                        String.format(BOOKING_NOT_FOUND, bookingId)
                ));
        UserDto userDto = UserMapper.toUserDto(booking.getItem().getUser());
        UserDto.BookerDto booker = UserMapper.toBookerDto(booking.getUser().getId());
        if (isCoincidence(booker.getId(), userId) || isCoincidence(userDto.getId(), userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("Информация доступна только для участников бронирования");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllByUserId(
            long userId,
            StateType state,
            int from,
            int size
    ) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    USER_NOT_FOUND, userId
            ));
        }
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(
                from / size, size, Sort.by(Sort.Direction.DESC, "start")
        );

        switch (state) {
            case ALL:
                bookings = repository.findAllByUserId(userId, pageable);
                break;
            case FUTURE:
                bookings = repository.findAllFutureByUserId(userId, pageable);
                break;
            case CURRENT:
                bookings = repository.findAllByUserIdAndStateCurrent(userId, pageable);
                break;
            case PAST:
                bookings = repository.findAllPastByUserId(userId, pageable);
                break;
            case WAITING:
                bookings = repository.findAllByUserIdAndStateType(userId, StateType.WAITING, pageable);
                break;
            case REJECTED:
                bookings = repository.findAllByUserIdAndStateType(userId, StateType.REJECTED, pageable);
                break;
        }
        return BookingMapper.toBookingsDto(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllByOwnerId(
            long userId,
            StateType state,
            int from,
            int size
    ) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    USER_NOT_FOUND, userId
            ));
        }
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(
                from / size, size, Sort.by(Sort.Direction.DESC, "start")
        );

        switch (state) {
            case ALL:
                bookings = repository.findAllByItemUserId(userId, pageable);
                break;
            case FUTURE:
                bookings = repository.findAllFutureByOwnerId(userId, pageable);
                break;
            case CURRENT:
                bookings = repository.findAllCurrentByOwnerId(userId, pageable);
                break;
            case PAST:
                bookings = repository.findAllPastByOwnerId(userId, pageable);
                break;
            case WAITING:
                bookings = repository
                        .findAllByItemUserIdAndStateType(userId, StateType.WAITING, pageable);
                break;
            case REJECTED:
                bookings = repository
                        .findAllByItemUserIdAndStateType(userId, StateType.REJECTED, pageable);
                break;
        }
        return BookingMapper.toBookingsDto(bookings);
    }

    boolean isCoincidence(long ownerId, long userId) {
        return (ownerId == userId);
    }
}
