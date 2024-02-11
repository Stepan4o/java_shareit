package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingValidationDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
//    private final LocalDateTime NOW = LocalDateTime.now();

    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override

    public BookingDto add(BookingValidationDto bookingValidationDto, Long userId) {
        Item item = itemRepository.findById(bookingValidationDto.getItemId()).orElseThrow(
                () -> new NotFoundException(String.format(
                        "Вещь с id:%d не найдена", bookingValidationDto.getItemId()
                )));
        if (item.isAvailable()) {
            if (!Objects.equals(item.getUser().getId(), userId)) {
                User booker = userRepository.findById(userId).orElseThrow(
                        () -> new NotFoundException(String.format(
                                "Пользователь с id:%d не найден", userId
                        )));
                Booking booking = BookingMapper.toBooking(bookingValidationDto);
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

    @Override
    public BookingDto patchUpdate(Long userId, Long bookingId, boolean bool) {
        Booking booking = repository.findById(bookingId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Booking с id:%d не найден", bookingId)
                ));
        if (Objects.equals(booking.getItem().getUser().getId(), userId)) {

            if (booking.getStatus().equals(Status.WAITING)) {
                if (bool) {
                    booking.setStatus(Status.APPROVED);
                } else {
                    booking.setStatus(Status.REJECTED);
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
    public List<BookingDto> getAllByUserId(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    "Пользователь id:%d не найден", userId
            ));
        }
        List<Booking> res;
        switch (state) {
            case "ALL":
                res = repository.findAllByUserIdOrderByStartDesc(userId);
                break;
            case "FUTURE":
                res = repository.findAllFutureByUserId(userId, LocalDateTime.now());
                break;
            case "CURRENT":
                res = repository.findAllCurrentByUserId(userId, LocalDateTime.now());
                break;
            case "PAST":
                res = repository.findAllPastByUserId(userId, LocalDateTime.now());
                break;
            case "WAITING":
                res = repository.findAllByUserIdAndStatus(userId, Status.WAITING);
                break;
            case "REJECTED":
                res = repository.findAllByUserIdAndStatus(userId, Status.REJECTED);
                break;
            default:
                throw new NotAvailableException(String.format(
                        "Unknown state: %s", state
                ));
        }
        return BookingMapper.toBookingsDto(res);
    }

    @Override
    public List<BookingDto> getAllByOwnerId(Long userId, String state) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format(
                    "Пользователь id:%d не найден", userId)
            );
        }
        List<Booking> res;
        switch (state) {
            case "ALL":
                res = repository.findAllByItemUserIdOrderByStartDesc(userId);
                break;

            case "FUTURE":
                res = repository.findAllFutureByOwnerId(userId, LocalDateTime.now());
                break;

            case "CURRENT":
                res = repository.findAllCurrentByOwnerId(userId, LocalDateTime.now());
                break;

            case "PAST":
                res = repository.findAllPastByOwnerId(userId, LocalDateTime.now());
                break;

            case "WAITING":
                res = repository.
                        findAllByItemUserIdAndStatus(userId, Status.WAITING);
                break;

            case "REJECTED":
                res = repository
                        .findAllByItemUserIdAndStatus(userId, Status.REJECTED);
                break;

            default:
                throw new NotAvailableException(String.format(
                        "Unknown state: %s", state
                ));

        }
        return BookingMapper.toBookingsDto(res);
    }

}
