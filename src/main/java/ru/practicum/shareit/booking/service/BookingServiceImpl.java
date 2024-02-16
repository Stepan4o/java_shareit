package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto add(BookingDtoIn bookingDtoIn, Long userId) {
        Item item = itemRepository.findById(bookingDtoIn.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Вещь с id:%d не найдена", bookingDtoIn.getItemId()
                )));
        if (item.isAvailable()) {
            if (!Objects.equals(item.getUser().getId(), userId)) {
                User booker = userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException(String.format(
                                "Пользователь с id:%d не найден", userId
                        )));
                Booking booking = BookingMapper.toBooking(bookingDtoIn);
                booking.setItem(item);
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

    /**
     * @param bool переменная для подтверждения/отказа в бронировании.
     *             Метод работает только от владельца
     *             и с актуальным статусом брони WAITING
     */
    @Override
    public BookingDto patchUpdate(Long userId, Long bookingId, boolean bool) {
        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Booking с id:%d не найден", bookingId)
                ));
        if (Objects.equals(booking.getItem().getUser().getId(), userId)) {

            if (booking.getStateType().equals(StateType.WAITING)) {
                if (bool) {
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
                        String.format("Booking с id:%d не найден", bookingId)
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
    public List<BookingDto> getAllByUserId(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    "Пользователь id:%d не найден", userId
            ));
        }
        List<Booking> bookings = new ArrayList<>();

        StateType type = StateType.fromString(state).orElseThrow(
                () -> new NotAvailableException(String.format(
                        "Unknown state: %s", state
                )));
        switch (type) {
            case ALL:
                bookings = repository.findAllByUserIdOrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = repository.findAllFutureByUserId(userId);
                break;
            case CURRENT:
                bookings = repository.findAllCurrentByUserId(userId);
                break;
            case PAST:
                bookings = repository.findAllPastByUserId(userId);
                break;
            case WAITING:
                bookings = repository.findAllByUserIdAndStateType(userId, StateType.WAITING);
                break;
            case REJECTED:
                bookings = repository.findAllByUserIdAndStateType(userId, StateType.REJECTED);
                break;
        }
        return BookingMapper.toBookingsDto(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllByOwnerId(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    "Пользователь id:%d не найден", userId
            ));
        }
        List<Booking> bookings = new ArrayList<>();

        StateType type = StateType.fromString(state).orElseThrow(
                () -> new NotAvailableException(String.format(
                        "Unknown state: %s", state
                )));
        switch (type) {
            case ALL:
                bookings = repository.findAllByItemUserIdOrderByStartDesc(userId);
                break;
            case FUTURE:
                bookings = repository.findAllFutureByOwnerId(userId);
                break;
            case CURRENT:
                bookings = repository.findAllCurrentByOwnerId(userId);
                break;
            case PAST:
                bookings = repository.findAllPastByOwnerId(userId);
                break;
            case WAITING:
                bookings = repository
                        .findAllByItemUserIdAndStateType(userId, StateType.WAITING);
                break;
            case REJECTED:
                bookings = repository
                        .findAllByItemUserIdAndStateType(userId, StateType.REJECTED);
                break;
        }
        return BookingMapper.toBookingsDto(bookings);
    }
}
