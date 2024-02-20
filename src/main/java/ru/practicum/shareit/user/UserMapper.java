package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDtoIn;

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

    public UserDto.BookerDto toBookerDto(Long bookerId) {
        UserDto.BookerDto bookerDto = new UserDto.BookerDto();
        bookerDto.setId(bookerId);
        return bookerDto;
    }
}
