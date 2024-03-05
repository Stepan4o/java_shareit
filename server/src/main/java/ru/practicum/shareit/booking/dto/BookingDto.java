//package ru.practicum.shareit.booking.dto;
//
//import lombok.Builder;
//import lombok.Data;
//import ru.practicum.shareit.booking.model.StateType;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.user.dto.UserDto;
//
//import java.time.LocalDateTime;
//
//@Data
//@Builder
//public class BookingDto {
//    private Long id;
//    private ItemDto.ItemForBookingDto item;
//    private UserDto.BookerDto booker;
//    private LocalDateTime start;
//    private LocalDateTime end;
//    private StateType status;
//
//    @Data
//    @Builder
//    public static class LastBookingDto {
//        private Long id;
//        private Long bookerId;
//    }
//
//    @Data
//    @Builder
//    public static class NextBookingDto {
//        private Long id;
//        private Long bookerId;
//    }
//}
