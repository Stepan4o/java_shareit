package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {

        log.debug("GET: /users/{}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getUsers() {

        log.debug("GET: /users");
        return userService.getUsers();
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {

        log.debug("POST: /users");
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {

        log.debug("DEL: /users/{}", id);
        userService.deleteUserById(id);
    }

    @PatchMapping("/{id}")
    public UserDto patchUpdateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {

        log.debug("PATCH: /users/{}", id);
        return userService.patchUpdateUser(userDto, id);
    }
}
