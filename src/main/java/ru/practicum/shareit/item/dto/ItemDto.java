package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto.LastBookingDto lastBooking;
    private BookingDto.NextBookingDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;

    @Data
    @Builder
    public static class ItemForBookingDto {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    public static class ItemDtoShort {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
    }
}
