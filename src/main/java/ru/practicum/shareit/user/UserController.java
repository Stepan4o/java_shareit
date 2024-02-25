package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(@Validated(Create.class) @RequestBody UserDtoIn userDtoIn) {

        log.debug("POST: /users");
        return userService.add(userDtoIn);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {

        log.debug("GET: /users/{}", id);
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {

        log.debug("GET: /users");
        return userService.getAll();
    }

    @PatchMapping("/{id}")
    public UserDto patchUpdateUser(
            @PathVariable Long id,
            @Validated(Update.class) @RequestBody UserDtoIn userDtoIn
    ) {

        log.debug("PATCH: /users/{}", id);
        return userService.updateById(userDtoIn, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {

        log.debug("DEL: /users/{}", id);
        userService.deleteById(id);
    }
}
