package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDtoOut {
    private Long id;
    private String name;
    private String email;

    @Data
    public static class BookerDto {
        private Long id;
    }
}

