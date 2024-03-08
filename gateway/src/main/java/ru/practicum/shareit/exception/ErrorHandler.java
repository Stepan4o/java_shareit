package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(
            final MissingServletRequestParameterException exception
    ) {
        String param = exception.getParameterName();
        return new ErrorResponse(String.format("Передан некорректный параметр : %s", param));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception
    ) {
        FieldError error = Objects.requireNonNull(exception.getFieldError());
        log.error("Некорректные данные -> '{}' : {}",
                error.getField(),
                error.getDefaultMessage()
        );
        return new ErrorResponse(
                String.format("Некорректные данные -> '%s' : %s",
                        error.getField(),
                        error.getDefaultMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingDateTimeException(
            final NotValidBookingDateTime exception
    ) {
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValidStateException(
            final UnknownStateException exception
    ) {
        return new ErrorResponse(
                String.format(exception.getMessage(), exception.getState())
        );
    }
}
