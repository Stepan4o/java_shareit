package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDtoOut;
import ru.practicum.shareit.user.dto.UserDtoIn;

import java.util.List;

public interface UserService {
    UserDtoOut getById(Long userId);

    UserDtoOut add(UserDtoIn userDtoIn);

    List<UserDtoOut> getAll();

    UserDtoOut updateById(UserDtoIn userDtoIn, Long userId);

    void deleteById(Long userId);
}
