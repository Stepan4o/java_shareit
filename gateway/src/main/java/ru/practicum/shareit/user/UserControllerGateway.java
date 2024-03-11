package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.utils.Create;
import ru.practicum.shareit.utils.Update;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserControllerGateway {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addNewUser(
            @Validated(Create.class) @RequestBody UserDtoRequest userDtoRequest
    ) {

        log.debug("POST: /users");
        return userClient.addNewUser(userDtoRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {

        log.debug("GET: /users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(
            @PathVariable Long userId
    ) {

        log.debug("GET: /users/{}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUpdateUser(
            @PathVariable Long userId,
            @Validated(Update.class) @RequestBody UserDtoRequest userDtoRequest
    ) {

        log.debug("PATCH: /users/{}", userId);
        return userClient.patchUpdateUser(userId, userDtoRequest);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(
            @PathVariable Long userId
    ) {

        log.debug("DELETE: /users/{}", userId);
        return userClient.deleteUserById(userId);
    }
}
