//package ru.practicum.shareit.item;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Pageable;
//import ru.practicum.shareit.booking.BookingRepository;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.StateType;
//import ru.practicum.shareit.exception.AccessDeniedException;
//import ru.practicum.shareit.exception.NotAvailableException;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.dto.CommentDto;
//import ru.practicum.shareit.item.dto.CommentDtoIn;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.ItemDtoIn;
//import ru.practicum.shareit.item.mapper.CommentMapper;
//import ru.practicum.shareit.item.mapper.ItemMapper;
//import ru.practicum.shareit.item.model.Comment;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.CommentRepository;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.item.service.ItemServiceImpl;
//import ru.practicum.shareit.request.ItemRequestRepository;
//import ru.practicum.shareit.request.model.ItemRequest;
//import ru.practicum.shareit.user.UserRepository;
//import ru.practicum.shareit.user.model.User;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static ru.practicum.shareit.utils.Constants.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ItemServiceTest {
//    @Mock
//    private ItemRepository itemRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private BookingRepository bookingRepository;
//    @Mock
//    private CommentRepository commentRepository;
//    @Mock
//    private ItemRequestRepository requestRepository;
//    @InjectMocks
//    private ItemServiceImpl itemService;
//
//    private final Long incorrectId = 100L;
//    private final Long id = 1L;
//    private final String name = "item";
//    private final String description = "description";
//    private final User user = new User(id, "user", "email@email.com");
//    private final ItemDtoIn itemDtoIn = ItemDtoIn.builder()
//            .name(name)
//            .description(description)
//            .available(true)
//            .build();
//
//    ItemRequest request = ItemRequest.builder()
//            .id(id)
//            .description("description")
//            .created(LocalDateTime.now())
//            .user(user)
//            .build();
//
//    ItemDtoIn itemDtoInWithRequest = ItemDtoIn.builder()
//            .name(name)
//            .description(description)
//            .available(true)
//            .requestId(id)
//            .build();
//
//    private final Item item = Item.builder()
//            .id(id)
//            .name(name)
//            .description(description)
//            .available(true)
//            .user(user)
//            .build();
//
//    Item itemWithRequest = Item.builder()
//            .id(id)
//            .name(name)
//            .description(description)
//            .available(true)
//            .user(user)
//            .itemRequest(request)
//            .build();
//
//    ItemDtoIn itemDtoForUpdate = ItemDtoIn.builder()
//            .name("NEW NAME")
//            .description("NEW DESCRIPTION")
//            .available(false)
//            .build();
//    private final Booking booking = Booking.builder()
//            .id(id)
//            .start(LocalDateTime.now().plusHours(1))
//            .end(LocalDateTime.now().plusHours(2))
//            .user(user)
//            .item(item)
//            .stateType(StateType.WAITING)
//            .build();
//
//    private final Comment comment = Comment.builder()
//            .id(id)
//            .text("text")
//            .item(item)
//            .author(user)
//            .created(LocalDateTime.now())
//            .build();
//
//    @Test
//    void add_itemShouldBEAddedIfUserWasFound() {
//        when(userRepository.findById(user.getId()))
//                .thenReturn(Optional.of(user));
//        when(itemRepository.save(any())).thenReturn(item);
//
//        ItemDto actual = itemService.add(itemDtoIn, user.getId());
//
//        Assertions.assertEquals(ItemMapper.toItemDto(item), actual);
//    }
//
//    @Test
//    void add_whenUserNotFoundShouldBeThrownUserNotFoundException() {
//        when(userRepository.findById(incorrectId))
//                .thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemService.add(itemDtoIn, incorrectId)
//        );
//        assertEquals(String.format(USER_NOT_FOUND, incorrectId), exception.getMessage());
//    }
//
//    @Test
//    void add_addingItemWithRequestIfItemRequestWasFound() {
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        when(requestRepository.findById(id)).thenReturn(Optional.of(request));
//        when(itemRepository.save(any())).thenReturn(itemWithRequest);
//
//        ItemDto actual = itemService.add(itemDtoInWithRequest, id);
//        assertNotNull(actual);
//        assertEquals(ItemMapper.toItemDto(itemWithRequest), actual);
//    }
//
//    @Test
//    void add_newItemShouldNotAddedIfItemRequestNotFound() {
//        ItemDtoIn itemDtoInWithRequestIncorrectId = ItemDtoIn.builder()
//                .name(name)
//                .description(description)
//                .available(true)
//                .requestId(incorrectId)
//                .build();
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        when(requestRepository.findById(incorrectId)).thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemService.add(itemDtoInWithRequestIncorrectId, id)
//        );
//        assertNotNull(exception.getMessage());
//        assertEquals(String.format(ITEM_REQUEST_NOT_FOUND, incorrectId), exception.getMessage());
//    }
//
//    @Test
//    void getItemById_shouldReturnItemDtoIfExist() {
//        when(bookingRepository.findNextBookingByItemId(anyLong(), any()))
//                .thenReturn(List.of(booking));
//        when(bookingRepository.findLastBookingByItemId(anyLong(), any()))
//                .thenReturn(List.of(booking));
//        when(commentRepository.findAllByItemId(anyLong()))
//                .thenReturn(List.of(comment));
//        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
//
//        ItemDto actual = itemService.getItemById(id, user.getId());
//        ItemDto expecting = ItemMapper.toItemDto(item);
//
//        assertAll(
//                () -> assertNotNull(actual),
//                () -> assertEquals(expecting.getName(), actual.getName())
//        );
//    }
//
//    @Test
//    void update_itemShouldBeUpdatedIfFieldsIsValidAndUserIsOwner() {
//        ItemDtoIn itemDtoForUpdate = ItemDtoIn.builder()
//                .name("NEW NAME")
//                .description("NEW DESCRIPTION")
//                .available(false)
//                .build();
//        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
//
//        ItemDto itemDtoUpdated = itemService.update(itemDtoForUpdate, id, id);
//        assertAll(
//                () -> assertNotNull(itemDtoUpdated),
//                () -> assertEquals(itemDtoUpdated.getName(), itemDtoForUpdate.getName()),
//                () -> assertEquals(itemDtoUpdated.getDescription(), itemDtoForUpdate.getDescription()),
//                () -> assertEquals(itemDtoUpdated.getAvailable(), itemDtoForUpdate.getAvailable())
//        );
//    }
//
//    @Test
//    void update_shouldBeThrownAccessDeniedExceptionIfUserNotOwner() {
//        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
//
//        Throwable exception = assertThrows(
//                AccessDeniedException.class,
//                () -> itemService.update(itemDtoForUpdate, id, incorrectId));
//        assertAll(
//                () -> assertNotNull(exception.getMessage()),
//                () -> assertEquals(OWNERS_ONLY, exception.getMessage())
//        );
//    }
//
//    @Test
//    void getItemById_shouldThrowNotFoundExceptionIfNotExist() {
//        when(itemRepository.findById(incorrectId))
//                .thenReturn(Optional.empty());
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemService.getItemById(incorrectId, user.getId())
//        );
//        assertEquals(String.format(ITEM_NOT_FOUND, incorrectId), exception.getMessage());
//    }
//
//    @Test
//    void getItemsBySubstring_shouldReturnNotEmptyListIfTextNotBlank() {
//        when(userRepository.existsById(id)).thenReturn(true);
//        when(itemRepository
//                .findAllByAvailableTrueAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(
//                        anyString(),
//                        anyString(),
//                        any(Pageable.class)
//                )
//        ).thenReturn(List.of(item));
//
//        List<ItemDto> actual = itemService.getItemsBySubstring("text", id, 1, 1);
//
//        assertNotNull(actual.get(0));
//    }
//
//    @Test
//    void getItemsBySubstring_shouldReturnEmptyListIfTextIsBlank() {
//        when(userRepository.existsById(id)).thenReturn(true);
//        List<ItemDto> actual = itemService.getItemsBySubstring("", id, 1, 1);
//
//        assertEquals(actual.size(), 0);
//    }
//
//    @Test
//    void getItemsBySubstring_shouldThrowNotFoundIfUserNotExist() {
//        when(userRepository.existsById(incorrectId)).thenReturn(false);
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemService.getItemsBySubstring("text", incorrectId, 1, 1)
//        );
//        assertAll(
//                () -> assertEquals(String.format(USER_NOT_FOUND, incorrectId), exception.getMessage()),
//                () -> assertNotNull(exception.getMessage())
//        );
//    }
//
//    @Test
//    void getAllItemsByUserId() {
//        when(userRepository.findById(anyLong()))
//                .thenReturn(Optional.of(user));
//        when(itemRepository.findByUserId(anyLong(), any(Pageable.class)))
//                .thenReturn(List.of(item));
//        when(commentRepository.findAllByItemId(anyLong()))
//                .thenReturn(List.of(comment));
//        when(bookingRepository.findNextBookingByItemId(anyLong(), any()))
//                .thenReturn(List.of(booking));
//        when(bookingRepository.findLastBookingByItemId(anyLong(), any()))
//                .thenReturn(List.of(booking));
//
//        List<ItemDto> actualDtoList = itemService.getAllItemsByUserId(id, 1, 1);
//
//        assertAll(
//                () -> assertNotNull(actualDtoList.get(0)),
//                () -> assertEquals(1, actualDtoList.size())
//        );
//    }
//
//    @Test
//    void addNewComment() {
//        when(bookingRepository.findFirstByUserIdAndItemIdAndEndBeforeAndStateType(
//                anyLong(), anyLong(), any(), any())
//        ).thenReturn(Optional.of(booking));
//        when(commentRepository.save(any())).thenReturn(comment);
//
//        CommentDto actual = itemService.addComment(
//                id,
//                CommentDtoIn.builder().text("text").create(LocalDateTime.now()).build(),
//                id);
//
//        assertAll(
//                () -> assertNotNull(actual),
//                () -> assertEquals(CommentMapper.toCommentDto(comment), actual)
//        );
//    }
//
//    @Test
//    void addComment_thenThrowExceptionIfUserNotOwner() {
//        when(bookingRepository.findFirstByUserIdAndItemIdAndEndBeforeAndStateType(
//                anyLong(), anyLong(), any(), any())
//        ).thenReturn(Optional.empty());
//
//        CommentDtoIn actual = CommentDtoIn.builder().text("text").build();
//
//        Throwable exception = assertThrows(
//                NotAvailableException.class,
//                () -> itemService.addComment(id, actual, id)
//        );
//        assertAll(
//                () -> assertNotNull(exception),
//                () -> assertEquals("Добавление комметария невозможно", exception.getMessage())
//        );
//    }
//}
