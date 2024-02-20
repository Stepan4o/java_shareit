package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@Builder
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
