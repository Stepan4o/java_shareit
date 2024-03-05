package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoIn;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(@RequestBody UserDtoIn userDtoIn) {

        return userService.add(userDtoIn);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {

        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {

        return userService.getAll();
    }

    @PatchMapping("/{id}")
    public UserDto patchUpdateUser(
            @PathVariable Long id,
            @RequestBody UserDtoIn userDtoIn
    ) {

        return userService.updateById(userDtoIn, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {

        userService.deleteById(id);
    }
}
