package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @MockBean
    private UserServiceImpl service;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private final UserDto firstUser = new UserDto(1L, "First", "Firstl@email.com");
    private final UserDto secondUser = new UserDto(2L, "Second", "Second@email.com");
    private final UserDto blankName = new UserDto(3L, "", "Second@email.com");
    private final UserDto invalidEmail = new UserDto(4L, "Second", "invalidEmail");

    @Test
    @SneakyThrows
    void statusShouldBeBadRequestWhenTryToSaveInvalidEmail() {
        mockMvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(invalidEmail))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void statusShouldBeBadRequestWhenTryToSaveInvalidName() {
        mockMvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(blankName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void getAllUsers() {
        when(service.getAll()).thenReturn(List.of(firstUser, secondUser));

        mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$[0].email", is(firstUser.getEmail()), String.class))
                .andExpect(jsonPath("$[1].id", is(secondUser.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(secondUser.getName()), String.class))
                .andExpect(jsonPath("$[1].email", is(secondUser.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void getUserById() {
        when(service.getById(anyLong())).thenReturn(firstUser);

        mockMvc.perform(get("/users/1", firstUser.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void addNewUser() {
        when(service.add(any())).thenReturn(firstUser);

        mockMvc.perform(post("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(firstUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void patchUpdateUser() {
        when(service.updateById(any(), anyLong())).thenReturn(firstUser);

        mockMvc.perform(patch("/users/1", firstUser.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(firstUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(firstUser.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(firstUser.getName()), String.class))
                .andExpect(jsonPath("$.email", is(firstUser.getEmail()), String.class));
    }

    @Test
    @SneakyThrows
    void deleteUserById() {
        mockMvc.perform(delete("/users/1")).andExpect(status().isOk());
        Mockito.verify(service, times(1)).deleteById(anyLong());
    }
}
