package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    @Override
    public UserDto getUserById(Long id) {
        User user = repository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пользователь с id:%d не найден", id)
                ));
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        return UserMapper.toUsersDto(users);
    }

    @Override
    public UserDto patchUpdateUser(UserDto userDto, Long id) {
        User user = repository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format("Пользователь с id:%d не найден", id)
                ));

        User updatedUser = updateUserFields(user, userDto);
        repository.save(updatedUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }

    private User updateUserFields(User user, UserDto userDto) {
        Map<String, BiConsumer<User, UserDto>> fieldsUpdaters = new HashMap<>();
        fieldsUpdaters.put("name", (u, uDto) -> u.setName(uDto.getName()));
        fieldsUpdaters.put("email", (u, uDto) -> u.setEmail(uDto.getEmail()));

        fieldsUpdaters.forEach((property, updaters) -> {
            switch (property) {
                case "name":
                    if (userDto.getName() != null) {
                        updaters.accept(user, userDto);
                    }
                    break;
                case "email":
                    if (userDto.getEmail() != null) {
                        updaters.accept(user, userDto);
                    }
                    break;
            }
        });
        return user;
//        propertyUpdaters.put("name", (u, uDto) -> {
//            if (uDto.getName() != null) {
//                u.setName(uDto.getName());
//            }
//        });
//
//        propertyUpdaters.put("email", (u, uDto) -> {
//            if (uDto.getEmail() != null) {
//                u.setEmail(uDto.getEmail());
//            }
//        });
//        propertyUpdaters.forEach((property, updaters) -> {
//                    updaters.accept(user, userDto);
//                }
//        );
//        return user;

    }
}
