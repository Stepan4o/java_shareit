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
        return UserMapper.userDto(repository.findById(id));
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.user(userDto);
        return UserMapper.userDto(repository.save(user));
    }

    @Override
    public List<UserDto> getUsers() {
        return repository.findAll()
                .stream()
                .map(UserMapper::userDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto patchUpdateUser(UserDto userDto, Long id) {
        User user = UserMapper.user(userDto);
        return UserMapper.userDto(repository.update(user, id));
    }

    @Override
    public void deleteUserById(Long id) {
        repository.deleteById(id);
    }
}
