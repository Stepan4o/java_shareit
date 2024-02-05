package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
//    User findById(Long id);
//
//    User save(User user);
//
//    List<User> findAll();
//
//    User update(User user, Long id);
//
//    void deleteById(Long id);
}
