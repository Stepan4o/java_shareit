package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User findById(Long id) {
        ifUserExist(id);
        return users.get(id);
    }

    @Override
    public User save(User user) {
        checkEmail(user.getEmail(), user.getId());
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user, Long id) {
        ifUserExist(id);
        User userForUpdate = findById(id);

        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkEmail(user.getEmail(), id);
            userForUpdate.setEmail(user.getEmail());
        }
        users.put(id, userForUpdate);
        return userForUpdate;
    }

    @Override
    public void deleteById(Long id) {
        ifUserExist(id);
        users.remove(id);
    }

    private void checkEmail(String email, Long id) {
        Set<String> emails = new HashSet<>();
        for (User u : users.values()) {
            if (!Objects.equals(id, u.getId())) {
                emails.add(u.getEmail());
            }
        }
        if (emails.contains(email)) {
            throw new AlreadyExistException(String.format(
                    "Такой email:%s уже существует", email
            ));
        }
    }

    private void ifUserExist(Long id) {
        if (!users.containsKey(id))
            throw new NotFoundException(String.format(
                    "Пользователь с id:%d не найден", id
            ));
    }
}
