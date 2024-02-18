package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {






    // TODO собрать BAD_REQUEST в одну группу исключений






    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handeConstraintViolationException(
            final ConstraintViolationException exception
    ) {
        log.error(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception
    ) {
        FieldError error = Objects.requireNonNull(exception.getFieldError());
        log.error("Ошибка валидации! Поле '{}' {}",
                error.getField(),
                error.getDefaultMessage()
        );
        return new ErrorResponse(
                String.format("Ошибка валидации! Поле '%s' %s",
                        error.getField(),
                        error.getDefaultMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(
            final AlreadyExistException exception
    ) {
        log.error(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse hanldeSqlException(
            final DataIntegrityViolationException exception
    ) {
        String message = Objects.requireNonNull(exception.getMessage());
        log.error(message);
        return new ErrorResponse("Некорректно указана информация");
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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeaderException(
            final MissingRequestHeaderException exception
    ) {
        log.error(exception.getMessage());
        return new ErrorResponse("Некорректное значение UserId");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotAvailableException(
            final NotAvailableException exception
    ) {
        log.error(exception.getMessage());
        return new ErrorResponse(exception.getMessage());
    }


}
