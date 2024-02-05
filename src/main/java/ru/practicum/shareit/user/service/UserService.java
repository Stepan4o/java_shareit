package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto patchUpdateUser(UserDto user, Long id);

    void deleteUserById(Long id);
}
