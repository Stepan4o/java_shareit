package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class NotAvailableException extends RuntimeException {
    private final String message;

    public NotAvailableException(String message) {
        this.message = message;
    }

}
