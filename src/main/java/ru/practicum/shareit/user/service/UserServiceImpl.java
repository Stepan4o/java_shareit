package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static ru.practicum.shareit.Constants.EMAIL_ALREADY_EXIST;
import static ru.practicum.shareit.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    @Override
    public UserDto getById(Long userId) {
        User savedUser = getUserIfExist(userId);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto add(UserDtoIn userDtoIn) {
        User newUser = UserMapper.toUser(userDtoIn);
        try {
            repository.save(newUser);
            return UserMapper.toUserDto(newUser);
        } catch (DataIntegrityViolationException exception) {
            throw new AlreadyExistException(String.format(
                    EMAIL_ALREADY_EXIST, userDtoIn.getEmail()
            ));
        }
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = repository.findAll();
        return UserMapper.toUsersDto(users);
    }

    @Override
    public UserDto updateById(UserDtoIn userDtoIn, Long userId) {
        User savedUser = getUserIfExist(userId);

        User updatedUser = updateUserFields(savedUser, userDtoIn);
        repository.save(updatedUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteById(Long userId) {
        repository.deleteById(userId);
    }

    private User updateUserFields(User user, UserDtoIn userDtoIn) {
        Map<String, BiConsumer<User, UserDtoIn>> fieldsUpdaters = new HashMap<>();
        fieldsUpdaters.put("name", (u, uDto) -> u.setName(uDto.getName()));
        fieldsUpdaters.put("email", (u, uDto) -> u.setEmail(uDto.getEmail()));

        fieldsUpdaters.forEach((property, updaters) -> {
            switch (property) {
                case "name":
                    if (userDtoIn.getName() != null) {
                        updaters.accept(user, userDtoIn);
                    }
                    break;
                case "email":
                    if (userDtoIn.getEmail() != null) {
                        updaters.accept(user, userDtoIn);
                    }
                    break;
            }
        });
        return user;
    }

    private User getUserIfExist(Long userId) {
        return repository.findById(userId).orElseThrow(
                () -> new NotFoundException(
                        String.format(USER_NOT_FOUND, userId)
                ));
    }
}
