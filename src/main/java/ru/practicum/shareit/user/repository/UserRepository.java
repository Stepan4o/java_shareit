package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    User findById(Long id);

    User save(User user);

    List<User> findAll();

    User update(User user, Long id);

    void deleteById(Long id);
}
