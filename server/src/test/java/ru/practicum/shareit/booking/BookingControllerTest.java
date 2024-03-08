package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.StateType;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.utils.Constants.HEADER_USER_ID;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;

    private final Long userId = 1L;
    private final BookingDtoIn bookingDtoIn = BookingDtoIn.builder()
            .start(LocalDateTime.now().plusHours(5))
            .end(LocalDateTime.now().plusHours(10))
            .itemId(1L)
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.now().plusHours(5))
            .end(LocalDateTime.now().plusHours(10))
            .booker(UserDto.BookerDto.builder().id(1L).build())
            .item(ItemDto.ItemForBookingDto.builder().id(1L).name("item").build())
            .build();
    private final BookingDtoIn bookingStartBeforeNow = BookingDtoIn.builder()
            .start(LocalDateTime.now().minusHours(5))
            .end(LocalDateTime.now().plusHours(10))
            .itemId(1L)
            .build();
    private final BookingDtoIn bookingNullStart = BookingDtoIn.builder()
            .end(LocalDateTime.now().plusHours(4))
            .itemId(1L)
            .build();

    @Test
    @SneakyThrows
    void addNewBooking_statusShouldBeOkWithValidFields() {
        when(bookingService.add(any(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDtoIn)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    @Test
    @SneakyThrows
    void getBooking_statusShouldBeOk() {
        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    @Test
    @SneakyThrows
    void approveBooking_statusShouldBeOk() {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookingDto)));
    }

    @Test
    @SneakyThrows
    void getAllByOwnerId_statusShouldBeOk() {
        when(bookingService
                .getAllByOwnerId(anyLong(), any(StateType.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner?state=ALL&from=1&size=1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    @SneakyThrows
    void gelAllByUserId_statusShouldBeOk() {
        when(bookingService.getAllByUserId(anyLong(), any(StateType.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings?state=ALL&from=1&size=1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, userId)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookingDto))));
    }
}
