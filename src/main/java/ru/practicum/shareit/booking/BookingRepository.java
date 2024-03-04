package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserIdOrderByStartDesc(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.user.id = ?1 " +
            "AND       b.start > now() " +
            "ORDER BY  b.start DESC ")
    List<Booking> findAllFutureByUserId(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.user.id = ?1 " +
            "AND       now() BETWEEN b.start AND b.end " +
            "ORDER BY  b.start DESC ")
    List<Booking> findAllCurrentByUserId(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.user.id = ?1 " +
            "AND       b.end < now() " +
            "ORDER BY  b.end DESC ")
    List<Booking> findAllPastByUserId(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.user.id = ?1 " +
            "AND       b.start > now() " +
            "ORDER BY  b.start DESC ")
    List<Booking> findAllFutureByOwnerId(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.user.id = ?1 " +
            "AND       now() BETWEEN b.start AND b.end " +
            "ORDER BY  b.start DESC ")
    List<Booking> findAllCurrentByOwnerId(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.user.id = ?1 " +
            "AND       b.end < now() " +
            "ORDER BY  b.start DESC ")
    List<Booking> findAllPastByOwnerId(Long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.id = ?1 " +
            "AND       b.start < now() " +
            "AND       b.stateType = ?2 " +
            "ORDER BY  b.start DESC ")
    List<Booking> findLastBookingByItemId(
            Long itemId,
            StateType stateType
    );

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.id = ?1 " +
            "AND       b.start > now() " +
            "AND       b.stateType = ?2 " +
            "ORDER BY  b.start ASC ")
    List<Booking> findNextBookingByItemId(
            Long itemId,
            StateType stateType
    );

    Optional<Booking> findBookingByIdAndItemUserId(Long itemId, Long userId);

    List<Booking> findAllByItemUserIdAndStateType(
            Long userId,
            StateType stateType,
            Pageable pageable
    );

    List<Booking> findAllByUserIdAndStateType(
            Long userId,
            StateType stateType,
            Pageable pageable
    );

    List<Booking> findAllByItemUserIdOrderByStartDesc(Long userId, Pageable pageable);

    Optional<Booking> findFirstByUserIdAndItemIdAndEndBeforeAndStateType(
            Long userId,
            Long itemId,
            LocalDateTime end,
            StateType stateType
    );
}
