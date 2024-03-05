//package ru.practicum.shareit.user;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.dto.UserDtoIn;
//import ru.practicum.shareit.user.service.UserServiceImpl;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = UserController.class)
//public class UserControllerTest {
//    @MockBean
//    private UserServiceImpl service;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private MockMvc mockMvc;
//
//    private final Long userId = 1L;
//    private final UserDtoIn userIn = UserDtoIn.builder().id(userId).name("First").email("Firstl@email.com").build();
//    private final UserDto firstUser = new UserDto(userId, "First", "Firstl@email.com");
//    private final UserDto secondUser = new UserDto(2L, "Second", "Second@email.com");
//    private final UserDto blankName = new UserDto(3L, "", "Second@email.com");
//    private final UserDto invalidEmail = new UserDto(4L, "Second", "invalidEmail");
//
//    @Test
//    @SneakyThrows
//    void addNewUser_whenTryToSaveInvalidEmail_statusShouldBeBadRequest() {
//        mockMvc.perform(post("/users")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(invalidEmail))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @SneakyThrows
//    void addNewUser_whenTryToSaveInvalidName_statusShouldBeBadRequest() {
//        mockMvc.perform(post("/users")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(blankName))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @SneakyThrows
//    void getAllUsers_statusOk() {
//        when(service.getAll()).thenReturn(List.of(firstUser, secondUser));
//
//        mockMvc.perform(get("/users")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(firstUser.getId()), Long.class))
//                .andExpect(jsonPath("$[0].name", is(firstUser.getName()), String.class))
//                .andExpect(jsonPath("$[0].email", is(firstUser.getEmail()), String.class))
//                .andExpect(jsonPath("$[1].id", is(secondUser.getId()), Long.class))
//                .andExpect(jsonPath("$[1].name", is(secondUser.getName()), String.class))
//                .andExpect(jsonPath("$[1].email", is(secondUser.getEmail()), String.class));
//    }
//
//    @Test
//    @SneakyThrows
//    void getUserById_statusOk() {
//        when(service.getById(userId)).thenReturn(firstUser);
//
//        String actual = mockMvc.perform(get("/users/{userId}", userId)
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//
//        assertNotNull(actual);
//        assertEquals(objectMapper.writeValueAsString(firstUser), actual);
//        verify(service).getById(firstUser.getId());
//    }
//
//    @Test
//    @SneakyThrows
//    void addNewUser_whenFieldsIsValid_thenStatusOk() {
//        when(service.add(userIn)).thenReturn(firstUser);
//
//        String actual = mockMvc.perform(post("/users")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(firstUser))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getRequest()
//                .getContentAsString();
//
//        assertNotNull(actual);
//        assertEquals(objectMapper.writeValueAsString(firstUser), actual);
//        verify(service).add(userIn);
//
//    }
//
//    @Test
//    @SneakyThrows
//    void patchUpdateUser_statusOk() {
//        when(service.updateById(any(), anyLong())).thenReturn(firstUser);
//
//        mockMvc.perform(patch("/users/{userId}", firstUser.getId())
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(firstUser))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getRequest()
//                .getContentAsString();
//
//        verify(service).updateById(any(), anyLong());
//    }
//
//    @Test
//    @SneakyThrows
//    void deleteUserById_statusOk() {
//        mockMvc.perform(delete("/users/{userId}", userId)).andExpect(status().isOk());
//
//        Mockito.verify(service, times(1)).deleteById(anyLong());
//    }
//
//    @Test
//    @SneakyThrows
//    void deleteUserById_whenUserNotExist_thenStatusBadRequestAndMethodIsNotInvoked() {
//        mockMvc.perform(delete("/users/")).andExpect(status().isMethodNotAllowed());
//
//        Mockito.verify(service, never()).deleteById(anyLong());
//    }
//}
