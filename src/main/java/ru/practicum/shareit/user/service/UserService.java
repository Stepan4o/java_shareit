package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUserById(Long id);

    UserDto addUser(UserDto userDto); //change

    List<UserDto> getUsers();

    UserDto patchUpdateUser(UserDto user, Long id);

    void deleteUserById(Long id);
}
