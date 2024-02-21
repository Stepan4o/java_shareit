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

import javax.validation.ConstraintViolationException;
import java.util.Objects;

import static ru.practicum.shareit.Constants.HEADER_USER_ID;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

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

    @ExceptionHandler({
            MissingRequestHeaderException.class,
            NotAvailableException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(
            final Exception exception
    ) {
        if (exception instanceof MissingServletRequestParameterException) {
            String param = ((MissingServletRequestParameterException) exception).getParameterName();
            return new ErrorResponse(String.format("Передан некорректный параметр : %s", param));

        } else if (exception instanceof MissingRequestHeaderException) {
            log.error("Incorrect header {}", HEADER_USER_ID);
            return new ErrorResponse(String.format(
                    "Incorrect header %s", HEADER_USER_ID
            ));

        } else {
            log.error(exception.getMessage());
            return new ErrorResponse(exception.getMessage());
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistsException(
            final AlreadyExistException exception
    ) {

        log.error(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(
            final NotFoundException exception
    ) {
        log.error(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(
            final AccessDeniedException exception
    ) {
        log.error(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }
}
