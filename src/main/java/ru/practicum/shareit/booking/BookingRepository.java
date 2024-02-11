package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserIdOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.user.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllFutureByUserId(
            Long userId,
            LocalDateTime now
    );

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.user.id = ?1 " +
            "AND   ?2 BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllCurrentByUserId(
            Long userId,
            LocalDateTime now
    );

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.user.id = ?1 " +
            "AND b.end < ?2 " +
            "ORDER BY b.end DESC ")
    List<Booking> findAllPastByUserId(Long userId, LocalDateTime now);

    List<Booking> findAllByUserIdAndStatus(Long userId, Status status);

    List<Booking> findAllByItemUserIdOrderByStartDesc(Long userId);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.user.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllFutureByOwnerId(Long userId, LocalDateTime start);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.user.id = ?1 " +
            "AND ?2 BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllCurrentByOwnerId(
            Long userId,
            LocalDateTime now
    );

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.user.id = ?1 " +
            "AND b.end < ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllPastByOwnerId(Long userId, LocalDateTime now);

    List<Booking> findAllByItemUserIdAndStatus(Long userId, Status status);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.id = ?1 " +
            "AND b.start < ?2 " +
            "AND b.status = ?3 " +
            "ORDER BY b.start DESC ")
    List<Booking> findLastBookingByItemId(Long itemId, LocalDateTime now, Status status);

    @Query("SELECT b FROM Booking AS b " +
            "WHERE b.item.id = ?1 " +
            "AND b.start > ?2 " +
            "AND b.status = ?3 " +
            "ORDER BY b.start ASC ")
    List<Booking> findNextBookingByItemId(Long itemId, LocalDateTime now, Status status);

    Optional<Booking> findFirstByUserIdAndItemIdAndEndBeforeAndStatus(Long userId, Long itemId, LocalDateTime end, Status status);
}
