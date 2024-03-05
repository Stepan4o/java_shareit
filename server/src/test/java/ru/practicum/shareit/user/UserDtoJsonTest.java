//package ru.practicum.shareit.user;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.json.JsonTest;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.boot.test.json.JsonContent;
//import ru.practicum.shareit.user.dto.UserDto;
//
//import java.io.IOException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@JsonTest
//public class UserDtoJsonTest {
//
//    @Autowired
//    JacksonTester<UserDto> json;
//
//    @Test
//    void testUserDto() throws IOException {
//        UserDto userDto = UserDto.builder()
//                .id(1L)
//                .name("userName")
//                .email("email@email.com")
//                .build();
//
//        JsonContent<UserDto> result = json.write(userDto);
//
//        Assertions.assertAll(
//                () -> assertThat(result)
//                        .extractingJsonPathNumberValue("$.id").isEqualTo(1),
//                () -> assertThat(result)
//                        .extractingJsonPathStringValue("$.name").isEqualTo("userName"),
//                () -> assertThat(result)
//                        .extractingJsonPathStringValue("$.email").isEqualTo("email@email.com")
//        );
//    }
//
//}
