package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoIn;

import java.util.List;

public interface UserService {
    UserDto getById(Long userId);

    UserDto add(UserDtoIn userDtoIn);

    List<UserDto> getAll();

    UserDto updateById(UserDtoIn userDtoIn, Long userId);

    void deleteById(Long userId);
}
