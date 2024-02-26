package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.utils.Constants.EMAIL_ALREADY_EXIST;
import static ru.practicum.shareit.utils.Constants.USER_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImpl service;

    private final long id = 1L, notFoundId = 2L;
    private final String name = "TestName", nameForUpdate = "UpdatedName";
    private final String email = "Test@email.com", emailForUpdate = "Updated@email.com";
    private final UserDtoIn userDtoIn = new UserDtoIn(id, name, email);
    private final User user = new User(id, name, email);

    @Test
    void getAll_shouldReturnListOfUser() {
        when(repository.findAll()).thenReturn(List.of(user));

        List<UserDto> targetUser = service.getAll();

        assertAll(
                () -> assertNotNull(targetUser),
                () -> assertEquals(1, targetUser.size())
        );
        verify(repository, times(1))
                .findAll();
    }

    @Test
    void getById_userShouldBeFoundedById() {
        when(repository.findById(id)).thenReturn(Optional.of(user));

        UserDto actualDto = service.getById(id);
        assertAll(
                () -> assertNotNull(actualDto),
                () -> assertEquals(UserMapper.toUserDto(user), actualDto)
        );
    }

    @Test
    void getById_whenUserNotFoundedByIdShouldBeThrownNotFoundException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(
                NotFoundException.class, () -> service.getById(notFoundId)
        );
        assertEquals(
                String.format(USER_NOT_FOUND, notFoundId),
                exception.getMessage(),
                "Сообщения не совпадают"
        );
    }

    @Test
    void add_userShouldBeAddedWhenNameIsValid() {
        when(repository.save(any())).thenReturn(user);

        UserDtoIn dtoForSave = new UserDtoIn();
        dtoForSave.setName(name);
        dtoForSave.setEmail(email);

        UserDto actualDto = service.add(dtoForSave);

        assertNotNull(actualDto.getId());
        assertEquals(UserMapper.toUserDto(user), actualDto);
    }

    @Test
    void add_whenAddedUserHasDuplicatedEmailShouldBeThrownAlreadyExistException() {
        doThrow(DataIntegrityViolationException.class).when(repository).save(any(User.class));

        Throwable exception = assertThrows(AlreadyExistException.class, () -> service.add(userDtoIn));
        assertAll(
                () -> assertNotNull(exception.getMessage(), "Сообщение не получено"),
                () -> assertEquals(String.format(EMAIL_ALREADY_EXIST, userDtoIn.getEmail()),
                        exception.getMessage(),
                        "Сообщения не совпадают"));
    }

    @Test
    void add_whenAddedUserWithInvalidNameShouldBeThrownConstraintViolationException() {
        doThrow(ConstraintViolationException.class).when(repository).save(any(User.class));

        assertThrows(ConstraintViolationException.class, () -> service.add(userDtoIn));
    }

    @Test
    void updateById_updatingUserShouldBeDoneOnlyWithAvailableFields() {
        when(repository.findById(id)).thenReturn(Optional.of(user));

        userDtoIn.setName(nameForUpdate);
        userDtoIn.setEmail(emailForUpdate);

        User savedUser = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        assertAll(
                () -> assertNotNull(savedUser),
                () -> assertNotEquals(savedUser.getName(), userDtoIn.getName()),
                () -> assertNotEquals(savedUser.getEmail(), userDtoIn.getEmail())
        );

        UserDto actualUserWithNewFields = service.updateById(userDtoIn, id);
        assertAll(
                () -> assertNotNull(actualUserWithNewFields),
                () -> assertEquals(UserMapper.toUserDto(savedUser), actualUserWithNewFields)
        );

        userDtoIn.setName(null);
        userDtoIn.setEmail(null);

        UserDto actualUserWithSameFields = service.updateById(userDtoIn, id);
        assertAll(
                () -> assertNotNull(actualUserWithSameFields.getName(), "name не должно быть null"),
                () -> assertNotNull(actualUserWithSameFields.getEmail(), "email не должно быть null"),
                () -> assertEquals(actualUserWithSameFields.getName(), nameForUpdate, "name не совпадает"),
                () -> assertEquals(actualUserWithSameFields.getEmail(), emailForUpdate, "email не совпадает")
        );
    }

    @Test
    void deleteById_shouldBeDeletedIfUserExist() {
        service.deleteById(id);
        verify(repository, times(1)).deleteById(id);
    }
}

