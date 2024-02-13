package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserDtoIn;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);

    UserDto createUser(UserDtoIn userDtoIn);

    List<UserDto> getAllUsers();

    UserDto patchUpdateUser(UserDtoIn userDtoIn, Long id);

    void deleteUserById(Long id);
}
