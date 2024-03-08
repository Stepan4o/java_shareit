package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class UnknownStateException extends RuntimeException {
    private final String message = "Unknown state: %s";
    private final String state;

    public UnknownStateException(String state) {
        this.state = state;
    }
}