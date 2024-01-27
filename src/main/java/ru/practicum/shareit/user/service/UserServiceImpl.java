package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository repository;

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(repository.findById(id));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.save(user));
    }

    @Override
    public List<UserDto> getUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto patchUpdateUser(UserDto userDto, Long id) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.update(user, id));
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
