package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.utils.Constants.EMAIL_ALREADY_EXIST;
import static ru.practicum.shareit.utils.Constants.USER_NOT_FOUND;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceIT {
    private final UserService userService;
    private final String name = "user", email = "email@email.com";
    private final UserDtoIn userIn = UserDtoIn.builder().name(name).email(email).build();
    private final UserDtoIn userInDuplicatedEmail = UserDtoIn.builder().name(name).email(email).build();

    @Test
    void add_whenAddingNewUserWithDuplicatedEmail_thenThrowException() {
        userService.add(userIn);

        Throwable exception = assertThrows(
                AlreadyExistException.class,
                () -> userService.add(userInDuplicatedEmail)
        );
        assertAll(
                () -> assertNotNull(exception),
                () -> assertNotNull(exception.getMessage()),
                () -> assertEquals(String.format(EMAIL_ALREADY_EXIST, email), exception.getMessage())
        );
    }

    @Test
    void add_gettingUserByIdAndUserNotFound_thenThrowNotFound() {
        long incorrectId = 100L;
        Throwable exception = assertThrows(
                NotFoundException.class,
                () -> userService.getById(incorrectId)
        );
        assertAll(
                () -> assertNotNull(exception),
                () -> assertNotNull(exception.getMessage()),
                () -> assertEquals(String.format(USER_NOT_FOUND, incorrectId), exception.getMessage())
        );
    }
}
