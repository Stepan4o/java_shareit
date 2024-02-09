package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserIdOrderByStartDesc(Long userId);

    List<Booking> findAllByUserIdAndStartIsAfterOrderByStartDesc(
            Long userId,
            LocalDateTime start
    );

    List<Booking> findAllByUserIdAndStartIsAfterAndEndIsBefore(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findAllByUserIdAndEndIsAfter(Long userId, LocalDateTime end);

    List<Booking> findAllByUserIdAndStatus(Long userId, Status status);
    //where bookingStateContaining waiting
    List<Booking> findAllByItemUserIdOrderByStartDesc(Long userId);

    List<Booking> findAllByItemUserIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime start);

    List<Booking> findAllByItemUserIdAndStartIsAfterAndEndIsBefore(
            Long userId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<Booking> findAllByItemUserIdAndEndIsAfter(Long userId, LocalDateTime end);

    List<Booking> findAllByItemUserIdAndStatus(Long userId, Status status);

    Booking findFirstByItemUserIdAndStartAfterOrderByStartAsc(Long userId, LocalDateTime start);
    Booking findFirstByItemUserIdAndEndBeforeOrderByEndAsc(Long userId, LocalDateTime end);
    Optional<Booking> findFirstByItemIdAndEndBeforeOrderByEndAsc(Long itemId, LocalDateTime end);
    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime start);
}
