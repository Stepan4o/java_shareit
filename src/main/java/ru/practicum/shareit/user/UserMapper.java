package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.UserBookerDto;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDtoIn;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserDtoIn userDtoIn) {
        User user = new User();
        user.setName(userDtoIn.getName());
        user.setEmail(userDtoIn.getEmail());
        return user;
    }

    public List<UserDto> toUsersDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserBookerDto toBookerDto(Long bookerId) {
        return UserBookerDto.builder()
                .id(bookerId)
                .build();
    }
}
