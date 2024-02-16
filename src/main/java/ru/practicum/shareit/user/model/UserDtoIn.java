package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class UserDtoIn {
    @NotBlank
    private String name;
    @NotEmpty
    @Email
    private String email;
}
