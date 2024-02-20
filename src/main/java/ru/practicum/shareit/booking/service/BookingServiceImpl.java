package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.Constants.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto add(BookingDtoIn bookingDtoIn, Long userId) {
        Item savedItem = itemRepository.findById(bookingDtoIn.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format(
                        ITEM_NOT_FOUND, bookingDtoIn.getItemId()
                )));
        if (savedItem.isAvailable()) {
            if (!Objects.equals(savedItem.getUser().getId(), userId)) {
                User booker = userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException(String.format(
                                USER_NOT_FOUND, userId
                        )));
                Booking booking = BookingMapper.toBooking(bookingDtoIn);
                booking.setItem(savedItem);
                booking.setUser(booker);
                repository.save(booking);
                return BookingMapper.toBookingDto(booking);
            } else {
                throw new NotFoundException("Не найдено");
            }
        } else {
            throw new NotAvailableException("Вещь не доступна для бронирования");
        }
    }

    @Override
    public BookingDto update(Long userId, Long bookingId, boolean isApprove) {
        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(
                        String.format(BOOKING_NOT_FOUND, bookingId)
                ));
        if (Objects.equals(booking.getItem().getUser().getId(), userId)) {

            if (booking.getStateType().equals(StateType.WAITING)) {
                if (isApprove) {
                    booking.setStateType(StateType.APPROVED);
                } else {
                    booking.setStateType(StateType.REJECTED);
                }
            } else {
                throw new NotAvailableException("Бронирование не в статус WAITING");
            }
            repository.save(booking);
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("Информация отсутствует");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getById(Long bookingId, Long userId) {
        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(
                        String.format(BOOKING_NOT_FOUND, bookingId)
                ));
        if (booking.getItem().getUser().getId().equals(userId) ||
                booking.getUser().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("Информация не найдена");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllByUserId(Long userId, String state, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    USER_NOT_FOUND, userId
            ));
        }
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);

        StateType type = StateType.fromString(state).orElseThrow(
                () -> new NotAvailableException(String.format(
                        UNKNOWN_STATE, state
                )));
        switch (type) {
            case ALL:
                bookings = repository.findAllByUserIdOrderByStartDesc(userId, pageable);
                break;
            case FUTURE:
                bookings = repository.findAllFutureByUserId(userId, pageable);
                break;
            case CURRENT:
                bookings = repository.findAllCurrentByUserId(userId, pageable);
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
    public List<BookingDto> getAllByOwnerId(Long userId,
                                            String state,
                                            Integer from,
                                            Integer size) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    USER_NOT_FOUND, userId
            ));
        }
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(from / size, size);

        StateType type = StateType.fromString(state).orElseThrow(
                () -> new NotAvailableException(String.format(
                        UNKNOWN_STATE, state
                )));
        switch (type) {
            case ALL:
                bookings = repository.findAllByItemUserIdOrderByStartDesc(userId, pageable);
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
}
