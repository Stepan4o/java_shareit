package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.model.UserBookerDto;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public List<UserDto> toUsersDto(Iterable<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for(User u : users) {
            usersDto.add(toUserDto(u));
        }
        return usersDto;
    }

    public UserBookerDto toBookerDto(User user) {
        return UserBookerDto.builder()
                .id(user.getId())
                .build();
    }
}
