//package ru.practicum.shareit.user;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.dto.UserDtoIn;
//import ru.practicum.shareit.user.model.User;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class UserMapperTest {
//
//    private final Long id1 = 1L;
//    private final String name1 = "name1";
//    private final String email1 = "1@email.com";
//
//    @Test
//    void toUserDtoTest() {
//        User user = new User(id1, name1, email1);
//        UserDto userDto = UserMapper.toUserDto(user);
//
//        assertAll(
//                () -> assertNotNull(user),
//                () -> assertNotNull(userDto),
//                () -> assertEquals(user.getId(), userDto.getId()),
//                () -> assertEquals(user.getName(), userDto.getName()),
//                () -> assertEquals(user.getEmail(), userDto.getEmail())
//        );
//    }
//
//    @Test
//    void toUserBookerTest() {
//        UserDto.BookerDto bookerDto = UserMapper.toBookerDto(id1);
//
//        assertAll(
//                () -> assertNotNull(bookerDto),
//                () -> assertEquals(bookerDto.getId(), id1)
//        );
//    }
//
//    @Test
//    void toUserTest() {
//        UserDtoIn userDtoIn = UserDtoIn.builder()
//                .name(name1)
//                .email(email1)
//                .build();
//
//        User user = UserMapper.toUser(userDtoIn);
//        user.setId(id1);
//
//        assertAll(
//                () -> assertEquals(user.getName(), userDtoIn.getName()),
//                () -> assertEquals(user.getEmail(), userDtoIn.getEmail())
//        );
//    }
//
//    @Test
//    void toUserDtoList() {
//        Long id2 = 2L;
//        String name2 = "name2";
//        String email2 = "2email.com";
//
//        List<User> userList = List.of(
//                new User(id1, name1, email1),
//                new User(id2, name2, email2)
//        );
//
//        List<UserDto> userDtoList = UserMapper.toUsersDto(userList);
//
//        assertAll(
//                () -> assertNotNull(userList.get(0)),
//                () -> assertNotNull(userList.get(1)),
//                () -> assertNotNull(userDtoList.get(0)),
//                () -> assertNotNull(userDtoList.get(1)),
//                () -> assertEquals(userList.get(0).getId(), userDtoList.get(0).getId()),
//                () -> assertEquals(userList.get(0).getName(), userDtoList.get(0).getName()),
//                () -> assertEquals(userList.get(0).getEmail(), userDtoList.get(0).getEmail()),
//                () -> assertEquals(userList.get(1).getId(), userDtoList.get(1).getId()),
//                () -> assertEquals(userList.get(1).getName(), userDtoList.get(1).getName()),
//                () -> assertEquals(userList.get(1).getEmail(), userDtoList.get(1).getEmail())
//        );
//    }
//}
