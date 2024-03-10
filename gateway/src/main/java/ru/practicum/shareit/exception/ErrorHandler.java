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

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            UnknownStateException.class,
            MethodArgumentNotValidException.class,
            NotValidBookingDateTime.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(
            final Exception exception
    ) {
        if (exception instanceof MissingServletRequestParameterException) {
            String param = ((MissingServletRequestParameterException) exception).getParameterName();
            log.error("Передан некорректный параметр: {}", param);
            return new ErrorResponse(
                    String.format("Передан некорректный параметр: %s", param)
            );
        } else if (exception instanceof MissingRequestHeaderException) {
            String headerName = ((MissingRequestHeaderException) exception).getHeaderName();
            log.error("Некорректное значение {}", headerName);
            return new ErrorResponse(
                    String.format("Не корректное значение %s", headerName)
            );
        } else if (exception instanceof UnknownStateException) {
            String incorrectState = ((UnknownStateException) exception).getState();
            log.error("Передан некорректный параметр: {}", incorrectState);
            return new ErrorResponse(
                    String.format(exception.getMessage(), incorrectState)
            );
        } else if (exception instanceof MethodArgumentNotValidException) {
            FieldError error = Objects.requireNonNull(((MethodArgumentNotValidException) exception).getFieldError());
            log.error("Некорректные данные -> '{}' : {}", error.getField(), error.getDefaultMessage());
            return new ErrorResponse(
                    String.format("Некорректные данные -> '%s' : %s", error.getField(), error.getDefaultMessage())
            );
        } else {
            return new ErrorResponse(exception.getMessage());
        }
    }
}
