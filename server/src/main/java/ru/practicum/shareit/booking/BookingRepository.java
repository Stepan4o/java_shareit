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

    List<Booking> findAllByUserId(long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.user.id = ?1 " +
            "AND       b.start > now() ")
    List<Booking> findAllFutureByUserId(long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.user.id = ?1 " +
            "AND       current_timestamp BETWEEN b.start AND b.end ")
    List<Booking> findAllByUserIdAndStateCurrent(long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.user.id = ?1 " +
            "AND       b.end < now() ")
    List<Booking> findAllPastByUserId(long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.user.id = ?1 " +
            "AND       b.start > now() ")
    List<Booking> findAllFutureByOwnerId(long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.user.id = ?1 " +
            "AND       now() BETWEEN b.start AND b.end ")
    List<Booking> findAllCurrentByOwnerId(long userId, Pageable pageable);

    @Query("SELECT     b FROM Booking AS b " +
            "WHERE     b.item.user.id = ?1 " +
            "AND       b.end < now() ")
    List<Booking> findAllPastByOwnerId(long userId, Pageable pageable);

    Optional<Booking> findFirstByItemIdAndStartBeforeAndStateTypeOrderByStartDesc(
            long itemId,
            LocalDateTime start,
            StateType stateType
    );

    Optional<Booking> findFirstByItemIdAndStartAfterAndStateTypeOrderByStartAsc(
            long itemId,
            LocalDateTime start,
            StateType stateType
    );

    Optional<Booking> findBookingByIdAndItemUserId(long itemId, long userId);

    List<Booking> findAllByItemUserIdAndStateType(
            long userId,
            StateType stateType,
            Pageable pageable
    );

    List<Booking> findAllByUserIdAndStateType(
            long userId,
            StateType stateType,
            Pageable pageable
    );

    List<Booking> findAllByItemUserId(long userId, Pageable pageable);

    Optional<Booking> findFirstByUserIdAndItemIdAndEndBeforeAndStateType(
            long userId,
            long itemId,
            LocalDateTime end,
            StateType stateType
    );
}
