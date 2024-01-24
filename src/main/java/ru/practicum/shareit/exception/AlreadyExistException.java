package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {
    private final String message;

    public AlreadyExistException(String message) {
        this.message = message;
    }

}
