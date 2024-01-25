package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {
    private final String message;

    public AccessDeniedException(String message) {
        this.message = message;
    }
}
