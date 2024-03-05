package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

    @Data
    @Builder
    public static class BookerDto {
        private Long id;
    }
}

