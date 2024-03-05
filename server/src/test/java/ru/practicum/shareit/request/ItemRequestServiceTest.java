//package ru.practicum.shareit.request;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Pageable;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.repository.ItemRepository;
//import ru.practicum.shareit.request.dto.ItemRequestDto;
//import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
//import ru.practicum.shareit.request.model.ItemRequest;
//import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
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
//import static ru.practicum.shareit.utils.Constants.USER_NOT_FOUND;
//
//@ExtendWith(MockitoExtension.class)
//public class ItemRequestServiceTest {
//    @Mock
//    private ItemRequestRepository itemRequestRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private ItemRepository itemRepository;
//    @InjectMocks
//    private ItemRequestServiceImpl itemRequestService;
//
//    private final Long id = 1L;
//    private final Long incorrectId = 100L;
//    private final User user = new User(id, "name", "enail@email.com");
//    private final ItemRequest request = ItemRequest.builder()
//            .id(id)
//            .description("description")
//            .created(LocalDateTime.now())
//            .user(user)
//            .build();
//    private final Item item = Item.builder()
//            .id(id)
//            .name("item")
//            .description("description")
//            .available(true)
//            .user(user)
//            .itemRequest(request)
//            .build();
//
//    @Test
//    void addNewItemRequest_shouldNotBeAddedIfUserNotFound() {
//        when(userRepository.findById(incorrectId)).thenReturn(Optional.empty());
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemRequestService.addNewItemRequest(ItemRequestDtoIn.builder()
//                        .description("description")
//                        .build(), incorrectId));
//        assertNotNull(exception);
//        assertEquals(String.format(USER_NOT_FOUND, incorrectId), exception.getMessage());
//    }
//
//    @Test
//    void addNewItemRequest_shouldBeAddedIfFieldsIsValid() {
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        when(itemRequestRepository.save(any())).thenReturn(request);
//
//        ItemRequestDto actual = itemRequestService.addNewItemRequest(
//                ItemRequestDtoIn.builder()
//                        .description("description")
//                        .build(), id);
//        assertNotNull(actual);
//        assertEquals(ItemRequestMapper.toItemRequestDto(request), actual);
//    }
//
//    @Test
//    void getAllItemRequests_shouldBeDoneIfUserWasFound() {
//        when(itemRequestRepository.findAllByUserIdIsNot(
//                anyLong(), any(Pageable.class))).thenReturn(List.of(request));
//        when(itemRepository
//                .findAllByItemRequestIdOrderByItemRequestCreatedAsc(anyLong()))
//                .thenReturn(List.of(item));
//
//        List<ItemRequestDto> actual = itemRequestService.getAllItemRequests(id, 1, 1);
//        assertAll(
//                () -> assertNotNull(actual),
//                () -> assertEquals(actual.size(), 1)
//        );
//    }
//
//    @Test
//    void getItemRequestById_whenUserNotFoundThenThrowException() {
//        when(userRepository.existsById(incorrectId)).thenReturn(false);
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemRequestService.getItemRequestById(incorrectId, id)
//        );
//        assertNotNull(exception);
//        assertEquals(String.format(USER_NOT_FOUND, incorrectId), exception.getMessage());
//    }
//
//    @Test
//    void getItemRequestById_whenUserFound() {
//        when(userRepository.existsById(id)).thenReturn(true);
//        when(itemRequestRepository.findById(id)).thenReturn(Optional.of(request));
//
//        ItemRequestDto actual = itemRequestService.getItemRequestById(id, id);
//        assertNotNull(actual);
//    }
//
//    @Test
//    void getItemRequestById_whenOwnerByIdWasFound() {
//        when(userRepository.existsById(id)).thenReturn(true);
//        when(itemRepository
//                .findAllByItemRequestIdOrderByItemRequestCreatedAsc(anyLong()))
//                .thenReturn(List.of(item));
//        when(itemRequestRepository.findAllByUserId(anyLong())).thenReturn(List.of(request));
//
//        List<ItemRequestDto> actual = itemRequestService.getItemRequestsByOwnerId(id);
//        assertAll(
//                () -> assertNotNull(actual),
//                () -> assertEquals(actual.size(), 1)
//        );
//    }
//}
