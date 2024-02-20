package ru.practicum.shareit.user;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDtoOut;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDtoIn;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public UserDtoOut toUserDto(User user) {
        return UserDtoOut.builder()
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

    public List<UserDtoOut> toUsersDto(List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDtoOut.BookerDto toBookerDto(Long bookerId) {
        UserDtoOut.BookerDto bookerDto = new UserDtoOut.BookerDto();
        bookerDto.setId(bookerId);
        return bookerDto;
    }
}
