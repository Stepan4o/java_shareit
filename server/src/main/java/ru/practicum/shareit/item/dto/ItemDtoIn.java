//package ru.practicum.shareit.item.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import ru.practicum.shareit.utils.Create;
//import ru.practicum.shareit.utils.Update;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
//@Data
//@Builder
//@AllArgsConstructor
//public class ItemDtoIn {
//
//    @NotBlank(groups = {Create.class})
//    @Size(max = 255, groups = {Create.class, Update.class})
//    private String name;
//
//    @NotBlank(groups = {Create.class})
//    @Size(max = 255, groups = {Create.class, Update.class})
//    private String description;
//
//    @NotNull(groups = {Create.class})
//    private Boolean available;
//
//    private Long requestId;
//}
