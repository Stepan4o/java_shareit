//package ru.practicum.shareit.booking;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Pageable;
//import ru.practicum.shareit.booking.dto.BookingDto;
//import ru.practicum.shareit.booking.dto.BookingDtoIn;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.StateType;
//import ru.practicum.shareit.booking.service.BookingServiceImpl;
//import ru.practicum.shareit.exception.NotAvailableException;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.user.UserRepository;
//import ru.practicum.shareit.user.model.User;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static ru.practicum.shareit.utils.Constants.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookingServiceTest {
//    @Mock
//    private BookingRepository bookingRepository;
//    @Mock
//    private ItemRepository itemRepository;
//    @Mock
//    private UserRepository userRepository;
//    @InjectMocks
//    private BookingServiceImpl bookingService;
//
//    private final Long id = 1L;
//    private final Long incorrectId = 100L;
//    private final User firstUser = new User(id, "user1", "email1@email.com");
//    private final User secondUser = new User(2L, "user2", "email2@email.com");
//
//    private final Item item = Item.builder()
//            .id(id)
//            .name("name")
//            .description("description")
//            .available(true)
//            .user(firstUser)
//            .build();
//    private final BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
//            .itemId(id)
//            .start(LocalDateTime.now().plusHours(1))
//            .end(LocalDateTime.now().plusHours(2))
//            .build();
//    private final Booking booking = Booking.builder()
//            .id(id)
//            .start(LocalDateTime.now().plusHours(1))
//            .end(LocalDateTime.now().plusHours(2))
//            .stateType(StateType.WAITING)
//            .item(item)
//            .user(firstUser)
//            .build();
//
//    @Test
//    void add_newBookingWithValidFieldsStatusIsOk() {
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(secondUser));
//        when(bookingRepository.save(any())).thenReturn(booking);
//
//        BookingDto actualDto = bookingService.add(bookingDtoIn, 2L);
//        assertAll(
//                () -> assertNotNull(actualDto),
//                () -> assertEquals(BookingMapper.toBookingDto(booking), actualDto)
//        );
//    }
//
//    @Test
//    void add_newBookingShouldThrowExceptionIfBookerIsOwner() {
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.add(bookingDtoIn, id));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        "Невозможно забронировать свою вещь",
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void add_newBookingShouldThrowExceptionIfTimeInvalid() {
//        BookingDtoIn bookingDtoInInvalidTime = BookingDtoIn.builder()
//                .itemId(id)
//                .start(LocalDateTime.now().plusHours(3))
//                .end(LocalDateTime.now().plusHours(2))
//                .build();
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.of(secondUser));
//
//        Throwable exception = assertThrows(
//                NotAvailableException.class,
//                () -> bookingService.add(bookingDtoInInvalidTime, 2L));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        "Дата начала бронирования должна быть раньше даты его окончания",
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void add_newBookingShouldThrowExceptionIfBookerWasNotFound() {
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//        when(userRepository.findById(100L)).thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.add(bookingDtoIn, 100L));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(USER_NOT_FOUND, 100L),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void add_newBookingShouldThrowExceptionIfItemNotAvailable() {
//        Item itemNotAvailable = Item.builder()
//                .id(id)
//                .name("name")
//                .description("description")
//                .available(false)
//                .user(firstUser)
//                .build();
//        when(itemRepository.findById(id)).thenReturn(Optional.of(itemNotAvailable));
//
//        Throwable exception = assertThrows(
//                NotAvailableException.class,
//                () -> bookingService.add(bookingDtoIn, id));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        "Вещь не доступна для бронирования",
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void add_newBookingShouldThrowExceptionIfItemNotFound() {
//        BookingDtoIn bookingDtoInWIthItemNotFound = BookingDtoIn.builder()
//                .itemId(incorrectId)
//                .start(LocalDateTime.now().plusHours(1))
//                .end(LocalDateTime.now().plusHours(2))
//                .build();
//        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.add(bookingDtoInWIthItemNotFound, id));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(ITEM_NOT_FOUND, incorrectId),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void update_throwExceptionIfNotFound() {
//        when(bookingRepository.findBookingByIdAndItemUserId(anyLong(), anyLong()))
//                .thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.update(id, incorrectId, true));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(BOOKING_NOT_FOUND, incorrectId),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void update_throwExceptionIfStatusNotWaiting() {
//        Booking bookingNotWaiting = Booking.builder()
//                .id(id)
//                .start(LocalDateTime.now().plusHours(1))
//                .end(LocalDateTime.now().plusHours(2))
//                .stateType(StateType.APPROVED)
//                .item(item)
//                .user(firstUser)
//                .build();
//        when(bookingRepository.findBookingByIdAndItemUserId(anyLong(), anyLong()))
//                .thenReturn(Optional.of(bookingNotWaiting));
//
//        Throwable exception = assertThrows(
//                NotAvailableException.class,
//                () -> bookingService.update(id, id, true));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        IS_NOT_WAITING,
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void update_whenUpdatingNotOwner_thenThrowNotFoundException() {
//        when(bookingRepository.findBookingByIdAndItemUserId(anyLong(), anyLong()))
//                .thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.update(incorrectId, id, true));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(BOOKING_NOT_FOUND, id),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void update_shouldBeDoneIfAllFieldsIsValid() {
//        Booking bookingApproved = Booking.builder()
//                .id(id)
//                .start(LocalDateTime.now().plusHours(1))
//                .end(LocalDateTime.now().plusHours(2))
//                .stateType(StateType.APPROVED)
//                .item(item)
//                .user(firstUser)
//                .build();
//        when(bookingRepository.findBookingByIdAndItemUserId(anyLong(), anyLong()))
//                .thenReturn(Optional.of(booking));
//        when(bookingRepository.save(any())).thenReturn(bookingApproved);
//
//        BookingDto actual = bookingService.update(id, id, true);
//
//        assertAll(
//                () -> assertNotNull(actual),
//                () -> assertEquals(BookingMapper.toBookingDto(bookingApproved), actual)
//        );
//    }
//
//    @Test
//    void getById_shouldThrowExceptionIfNotFound() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.getById(incorrectId, id));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(BOOKING_NOT_FOUND, incorrectId),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void getById_shouldThrowExceptionIfIncorrectUserId() {
//        Item itemWithOtherUser = Item.builder()
//                .id(id)
//                .name("name")
//                .description("description")
//                .available(true)
//                .user(secondUser)
//                .build();
//
//        Booking bookingWithOtherUser = Booking.builder()
//                .id(id)
//                .start(LocalDateTime.now().plusHours(1))
//                .end(LocalDateTime.now().plusHours(2))
//                .stateType(StateType.WAITING)
//                .item(itemWithOtherUser)
//                .user(firstUser)
//                .build();
//
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWithOtherUser));
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.getById(id, 3L));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        "Информация доступна только для участников бронирования",
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void getById() {
//        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
//
//        BookingDto actual = bookingService.getById(id, id);
//        assertAll(
//                () -> assertNotNull(actual),
//                () -> assertEquals(BookingMapper.toBookingDto(booking), actual)
//        );
//    }
//
//    @Test
//    void getAllByUserId_stateAll() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllByUserIdOrderByStartDesc(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByUserId(id, "ALL", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByUserId_stateFuture() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllFutureByUserId(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByUserId(id, "FUTURE", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByUserId_stateCurrent() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllCurrentByUserId(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByUserId(id, "CURRENT", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByUserId_statePast() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllPastByUserId(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByUserId(id, "PAST", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByUserId_stateWaiting() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllByUserIdAndStateType(
//                anyLong(), any(StateType.class), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByUserId(id, "WAITING", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByUserId_stateRejected() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllByUserIdAndStateType(
//                anyLong(), any(StateType.class), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByUserId(id, "REJECTED", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByUserId_shouldThrownExceptionIfUserNotFound() {
//        when(userRepository.existsById(anyLong())).thenReturn(false);
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.getAllByUserId(incorrectId, "ALL", 1, 1));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(USER_NOT_FOUND, incorrectId),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void getAllByUserId_shouldThrownExceptionIfUnknownState() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//
//        Throwable exception = assertThrows(
//                NotAvailableException.class,
//                () -> bookingService.getAllByUserId(id, "???", 1, 1));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(UNKNOWN_STATE, "???"),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void getAllByOwnerId_stateAll() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllByItemUserIdOrderByStartDesc(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByOwnerId(id, "ALL", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByOwnerId_stateFuture() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllFutureByOwnerId(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByOwnerId(id, "FUTURE", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByOwnerId_stateCurrent() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllCurrentByOwnerId(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByOwnerId(id, "CURRENT", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByOwnerId_statePast() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllPastByOwnerId(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByOwnerId(id, "PAST", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByOwnerId_stateWaiting() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllByItemUserIdAndStateType(
//                anyLong(), any(StateType.class), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByOwnerId(id, "WAITING", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByOwnerId_stateRejected() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//        when(bookingRepository.findAllByItemUserIdAndStateType(
//                anyLong(), any(StateType.class), any(Pageable.class))).thenReturn(List.of(booking));
//
//        List<BookingDto> actual = bookingService.getAllByOwnerId(id, "REJECTED", 1, 1);
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getAllByOwnerId_shouldThrowExceptionIfUserNotFound() {
//        when(userRepository.existsById(anyLong())).thenReturn(false);
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> bookingService.getAllByOwnerId(incorrectId, "ALL", 1, 1));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(USER_NOT_FOUND, incorrectId),
//                        exception.getMessage()
//                )
//        );
//    }
//
//    @Test
//    void getAllByOwnerId_shouldThrowExceptionIfUnknownState() {
//        when(userRepository.existsById(anyLong())).thenReturn(true);
//
//        Throwable exception = assertThrows(
//                NotAvailableException.class,
//                () -> bookingService.getAllByOwnerId(id, "???", 1, 1));
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals(
//                        String.format(UNKNOWN_STATE, "???"),
//                        exception.getMessage()
//                )
//        );
//    }
//}
//
//
