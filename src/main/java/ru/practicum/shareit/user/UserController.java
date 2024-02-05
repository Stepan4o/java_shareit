package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.UserDto;
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
    public List<UserDto> getAllUsers() {

        log.debug("GET: /users");
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {

        log.debug("POST: /users");
        return userService.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {

        log.debug("DEL: /users/{}", id);
        userService.deleteUserById(id);
    }

    @PatchMapping("/{id}")
    public UserDto patchUpdateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto
    ) {

        log.debug("PATCH: /users/{}", id);
        return userService.patchUpdateUser(userDto, id);
    }
}
