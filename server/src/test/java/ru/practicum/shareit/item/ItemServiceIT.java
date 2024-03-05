//package ru.practicum.shareit.item;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.shareit.exception.NotFoundException;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.ItemDtoIn;
//import ru.practicum.shareit.item.service.ItemService;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.dto.UserDtoIn;
//import ru.practicum.shareit.user.service.UserService;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.junit.jupiter.api.Assertions.*;
//import static ru.practicum.shareit.utils.Constants.ITEM_NOT_FOUND;
//
//@Transactional
//@DirtiesContext
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
//public class ItemServiceIT {
//    private final ItemService itemService;
//    private final UserService userService;
//    private final String itemName = "item", itemDescription = "description";
//    private final UserDtoIn userIn = UserDtoIn.builder().name("user").email("email@email.com").build();
//
//    private UserDto userDto;
//
//    @Test
//    void add_itemShouldBeAddedIfFieldIsValid() {
//        userDto = userService.add(userIn);
//        ItemDtoIn itemIn = ItemDtoIn.builder()
//                .name(itemName)
//                .description(itemDescription)
//                .available(true)
//                .build();
//
//        ItemDto itemDto = itemService.add(itemIn, userDto.getId());
//
//        assertNotNull(itemDto);
//        assertThat(itemDto.getId(), notNullValue());
//        assertThat(itemDto.getName(), equalTo(itemIn.getName()));
//        assertThat(itemDto.getDescription(), equalTo(itemIn.getDescription()));
//        userService.deleteById(userDto.getId());
//    }
//
//    @Test
//    void add_whenAddingItemWithNullName_thenThrowException() {
//        userDto = userService.add(userIn);
//        ItemDtoIn itemIn = ItemDtoIn.builder()
//                .description(itemDescription)
//                .available(true)
//                .build();
//
//        Throwable exception = assertThrows(
//                DataIntegrityViolationException.class,
//                () -> itemService.add(itemIn, userDto.getId()));
//
//        assertNotNull(exception.getMessage());
//        userService.deleteById(userDto.getId());
//    }
//
//    @Test
//    void add_whenAddingItemWithNullDescription_thenThrowException() {
//        userDto = userService.add(userIn);
//        ItemDtoIn itemIn = ItemDtoIn.builder()
//                .name(itemName)
//                .available(true)
//                .build();
//
//        Throwable exception = assertThrows(
//                DataIntegrityViolationException.class,
//                () -> itemService.add(itemIn, userDto.getId()));
//        assertNotNull(exception.getMessage());
//        userService.deleteById(userDto.getId());
//    }
//
//    @Test
//    void getItemById_whenItemByIdNotFound_thenThrowException() {
//        userDto = userService.add(userIn);
//        long invalidId = 100L;
//
//        Throwable exception = assertThrows(
//                NotFoundException.class,
//                () -> itemService.getItemById(invalidId, userDto.getId())
//        );
//        assertAll(
//                () -> assertNotNull(exception.getMessage()),
//                () -> assertEquals(String.format(ITEM_NOT_FOUND, invalidId), exception.getMessage())
//        );
//        userService.deleteById(userDto.getId());
//    }
//}
