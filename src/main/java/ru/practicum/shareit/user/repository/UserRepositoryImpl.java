package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.function.BiConsumer;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long id = 0L;

    @Override
    public User findById(Long id) {
        ensureUserExists(id);
        return users.get(id);
    }

    @Override
    public User save(User user) {
        checkEmail(user.getEmail());
        user.setId(++id);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(User user, Long id) {
        ensureUserExists(id);
        User userForUpdate = findById(id);

        Map<String, BiConsumer<User, User>> propertyUpdaters = new HashMap<>();
        propertyUpdaters.put("name", (savedUser, incomingUser) -> {
            if (incomingUser.getName() != null) {
                savedUser.setName(incomingUser.getName());
            }
        });

        propertyUpdaters.put("email", (savedUser, incomingUser) -> {
            String newEmail = incomingUser.getEmail();
            String oldEmail = savedUser.getEmail();

            // если входящий емаил такой же как и старый, то ничего не меняю
            if (newEmail != null && !oldEmail.equals(newEmail)) {
                updateEmailsStorage(oldEmail, newEmail);
                savedUser.setEmail(newEmail);
            }
        });

        propertyUpdaters.forEach((property, updaters) -> {
            updaters.accept(userForUpdate, user);
        });

        return userForUpdate;
    }

    @Override
    public void deleteById(Long id) {
        User savedUser = findById(id);
        emails.remove(savedUser.getEmail());
        users.remove(savedUser.getId());
    }

    private void checkEmail(String email) {
        if (emails.contains(email)) {
            throw new AlreadyExistException(String.format(
                    "Такой email:%s уже существует", email
            ));
        }
    }

    private void updateEmailsStorage(String oldEmail, String newEmail) {
        checkEmail(newEmail);
        emails.remove(oldEmail);
        emails.add(newEmail);
    }

    private void ensureUserExists(Long id) {
        if (!users.containsKey(id))
            throw new NotFoundException(String.format(
                    "Пользователь с id:%d не найден", id
            ));
    }
}
